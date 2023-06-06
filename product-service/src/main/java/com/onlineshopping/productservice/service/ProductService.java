package com.onlineshopping.productservice.service;

import com.onlineshopping.productservice.dto.ProductRequest;
import com.onlineshopping.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    void createProduct(ProductRequest productRequest);

    List<ProductResponse> getAll();
}
