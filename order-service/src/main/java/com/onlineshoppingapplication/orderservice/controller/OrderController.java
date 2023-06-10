package com.onlineshoppingapplication.orderservice.controller;

import com.onlineshoppingapplication.orderservice.dto.OrderRequest;
import com.onlineshoppingapplication.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallBackMethod")
    @TimeLimiter(name = "inventory") //CompleteblaFuture<String> dönüşüne TimeLimitter anotasyonu yüzünden geçtik. Order service dönüş tipimizide bu yüzden void'den String'e çevirdik, Order placed successfully mesajımızı OrderServiceImpl'de ürünü kaydettiğimiz if metodu içinde return ediyoruz artık.
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

    //fallBackMethod'u CircuitBreaker'ımız için oluşturduk. Inventory Service durmuşken 5 çağrı yaptığımızda bu metod çalışıyor. Çalışıp çalışmadığını localhost:8081/actuator/health den de kontrol edebiliriz.
    public CompletableFuture<String> fallBackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please order after some time!");
    }
}
