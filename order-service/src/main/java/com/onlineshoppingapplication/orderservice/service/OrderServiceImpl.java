package com.onlineshoppingapplication.orderservice.service;

import com.onlineshoppingapplication.orderservice.dto.InventoryResponse;
import com.onlineshoppingapplication.orderservice.dto.OrderLineItemsDto;
import com.onlineshoppingapplication.orderservice.dto.OrderRequest;
import com.onlineshoppingapplication.orderservice.event.OrderPlacedEvent;
import com.onlineshoppingapplication.orderservice.model.Order;
import com.onlineshoppingapplication.orderservice.model.OrderLineItems;
import com.onlineshoppingapplication.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Override
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString()); //Random bir orderNumber set'liyoruz.

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDo)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        //Below operations explanation: I want to collect all skuCode from the order object.
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        log.info("Calling inventory service.");

        //Zipkin trace yapısını kullanmak için aşağıdaki Span ve Tracer yapısını kullandık.
        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");

        try (Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())) {
            //Below operations are created for to call inventory service, place order if product is in stock.
            //bodyToMono is for to read data coming from webClient response. We get answer boolean so why we wrote boolean to the bodyToMono.
            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block(); //webClient will make a synchronous request to the uri endpoint.

            boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock); //inventoryResponseArray.st yazaıp otomatik stream le tamamladığımda ifadeyi Arrays.stream haline getiriyor. Ardından allMatch(inventoryResponse -> yapıp inventoryResponse.isInStock()) dedikten sonra sarı uyarıya tıklayıp introduce local variable diyip bu hale getirdim.

            if (allProductsInStock) {
                orderRepository.save(order); //eğer tüm ürünler stokta ise database'e kaydet.
                kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
                return "Order placed successfully!";
            } else {
                throw new IllegalArgumentException("Product is not in stock! Please try again later.");
            }
        } finally {
            inventoryServiceLookup.end();
        }
    }

    private OrderLineItems mapToDo(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;
    }
}
