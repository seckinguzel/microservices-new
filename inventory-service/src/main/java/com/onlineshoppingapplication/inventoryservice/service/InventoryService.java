package com.onlineshoppingapplication.inventoryservice.service;

import com.onlineshoppingapplication.inventoryservice.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> isInStock(List<String> skuCode);
}
