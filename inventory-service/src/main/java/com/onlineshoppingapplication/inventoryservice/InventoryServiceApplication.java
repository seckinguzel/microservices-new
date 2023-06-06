package com.onlineshoppingapplication.inventoryservice;

import com.onlineshoppingapplication.inventoryservice.model.Inventory;
import com.onlineshoppingapplication.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) { //When starts the application we will not have data so why we created this bean annotation.
        return args -> {
            Inventory inventory1 = new Inventory();
            inventory1.setSkuCode("iphone_13");
            inventory1.setQuantity(100);

            Inventory inventory2 = new Inventory();
            inventory2.setSkuCode("iphone_13_red");
            inventory2.setQuantity(0);

            inventoryRepository.save(inventory1);
            inventoryRepository.save(inventory2);
        };
    }
}
