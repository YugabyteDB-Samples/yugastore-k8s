package com.yugabyte.app.yugastore.controller;

import java.util.List;

import com.yugabyte.app.yugastore.domain.OrderCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yugabyte.app.yugastore.domain.ProductMetadata;
import com.yugabyte.app.yugastore.domain.OrderCount;
import com.yugabyte.app.yugastore.domain.ProductRanking;
import com.yugabyte.app.yugastore.service.ProductCatalogServiceRest;

/**
 * The controller that handles all calls related to the product catalog.
 */
@RestController
@RequestMapping(value = "/api/v1")
public class ProductCatalogController {

  // The REST service handler that interacts with the product catalog microservice.
  private final ProductCatalogServiceRest productCatalogServiceRest;

  @Autowired
  public ProductCatalogController(ProductCatalogServiceRest productCatalogServiceRest) {
    this.productCatalogServiceRest = productCatalogServiceRest;
  }


  /**
   * Return details of a single product.
   */
  @RequestMapping(method = RequestMethod.GET, value = "/product/{sku}", produces = "application/json")
  public @ResponseBody ResponseEntity<ProductMetadata> getProductDetails(@PathVariable("sku") String sku) {
    ProductMetadata productMetadata = productCatalogServiceRest.getProductDetails(sku);
    return new ResponseEntity<ProductMetadata>(productMetadata, HttpStatus.OK);
  }


  /**
   * Fetch a listing of products, given a limit and offset.
   */
  @RequestMapping(method = RequestMethod.GET, value = "/products", produces = "application/json")
  public @ResponseBody ResponseEntity<List<ProductMetadata>> getProducts(@Param("limit") int limit,
                                                                         @Param("offset") int offset) {
    List<ProductMetadata> products = productCatalogServiceRest.getProducts(limit, offset);
    return new ResponseEntity<List<ProductMetadata>>(products, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/products/category/{category}", produces = "application/json")
  public @ResponseBody ResponseEntity<List<ProductRanking>> getProductsByCategory(
      @PathVariable("category") String category,
      @Param("limit") int limit,
      @Param("offset") int offset) {
    List<ProductRanking> products = productCatalogServiceRest.getProductsByCategory(category, limit, offset);
    return new ResponseEntity<List<ProductRanking>>(products, HttpStatus.OK);
  }


  @RequestMapping(method = RequestMethod.GET, value = "/product/update/{sku}", produces = "application/json")
  public @ResponseBody ResponseEntity<String> updateProduct(@PathVariable String sku,
                              @Param("title") String title,
                              @Param("description") String description,
                              @Param("price") double price
  )
  {
    String response = productCatalogServiceRest.updateProduct(sku,title,description,price);
    return new ResponseEntity<String>(response,HttpStatus.OK);
  }


  /**
   * Return order count
   */
  @RequestMapping(method = RequestMethod.GET, value = "/orders", produces = "application/json")
  public @ResponseBody ResponseEntity<OrderCount> getOrderCount() {
    OrderCount orderCountData = productCatalogServiceRest.getOrderCount();
//    OrderCount orderCountData = new OrderCount();
//    orderCountData.setOrdercount(9999);

    return new ResponseEntity<OrderCount>(orderCountData, HttpStatus.OK);
  }
}
