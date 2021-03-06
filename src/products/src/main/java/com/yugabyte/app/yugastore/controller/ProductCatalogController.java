package com.yugabyte.app.yugastore.controller;

import java.util.List;

import com.yugabyte.app.yugastore.domain.OrderCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yugabyte.app.yugastore.domain.ProductMetadata;
import com.yugabyte.app.yugastore.domain.ProductRanking;
import com.yugabyte.app.yugastore.service.ProductService;
import com.yugabyte.app.yugastore.service.ProductRankingService;

@RestController
@RequestMapping(value = "/products-microservice")
public class ProductCatalogController {

  // This service is used to lookup metadata of products by their id.
  @Autowired
  ProductService productService;

  // This service is used to lookup the top products by sales rank in a category.
  @Autowired
  ProductRankingService productRankingService;

  @RequestMapping(method = RequestMethod.GET, value = "/product/{sku}", produces = "application/json")
  public ProductMetadata getProductDetails(@PathVariable String sku) {
    ProductMetadata productMetadata = productService.findById(sku).get();
    return productMetadata;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/products", produces = "application/json")
  public List<ProductMetadata> getProducts(@Param("limit") int limit,
    @Param("offset") int offset) {
    return productService.findAllProductsPageable(limit, offset);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/products/category/{category}", produces = "application/json")
  public List<ProductRanking> getProductsByCategory(@PathVariable String category,
                                                    @Param("limit") int limit,
                                                    @Param("offset") int offset) {
    return productRankingService.getProductsByCategory(category, limit, offset);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/product/update/{sku}", produces = "application/json")
  public String updateProduct(@PathVariable String sku,
                                       @Param("title") String title,
                                       @Param("description") String description,
                                       @Param("price") double price
                                       ) {
    String response = "Failed to update the record.";

    int result = productService.updateProduct(sku,title,description,price);

    if(result == 1){
      response = "Record updated successfully";
    }

    return response;
  }


  @RequestMapping(method = RequestMethod.GET, value = "/orders", produces = "application/json")
  public OrderCount getProductDetails() {
    int count = productService.getOrderCount();
    OrderCount orderCount = new OrderCount();
    orderCount.setOrdercount(count);
    return orderCount;
  }
}
