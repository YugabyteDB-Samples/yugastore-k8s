package com.yugabyte.app.yugastore.service;

import java.util.List;

import com.yugabyte.app.yugastore.domain.OrderCount;
import com.yugabyte.app.yugastore.domain.ProductMetadata;
import com.yugabyte.app.yugastore.domain.ProductRanking;

public interface ProductCatalogServiceRest {
  
  ProductMetadata getProductDetails(String sku);

  List<ProductMetadata> getProducts(int limit, int offset);

  List<ProductRanking> getProductsByCategory(String category, int limit, int offset);

    String updateProduct(String sku, String title, String description, double price);

    OrderCount getOrderCount();
}
