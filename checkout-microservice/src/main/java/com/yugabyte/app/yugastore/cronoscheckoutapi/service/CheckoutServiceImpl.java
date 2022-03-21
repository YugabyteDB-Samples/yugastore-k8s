package com.yugabyte.app.yugastore.cronoscheckoutapi.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
//import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.yugabyte.app.yugastore.cronoscheckoutapi.domain.Order;
import com.yugabyte.app.yugastore.cronoscheckoutapi.domain.ProductInventory;
import com.yugabyte.app.yugastore.cronoscheckoutapi.domain.ProductMetadata;
import com.yugabyte.app.yugastore.cronoscheckoutapi.exception.NotEnoughProductsInStockException;
import com.yugabyte.app.yugastore.cronoscheckoutapi.repositories.ProductInventoryRepository;
import com.yugabyte.app.yugastore.cronoscheckoutapi.rest.clients.ProductCatalogRestClient;
import com.yugabyte.app.yugastore.cronoscheckoutapi.rest.clients.ShoppingCartRestClient;


@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class CheckoutServiceImpl {

	private final ShoppingCartRestClient shoppingCartRestClient;
	private final ProductCatalogRestClient productCatalogRestClient;
	private final ProductInventoryRepository productInventoryRepository;

	ProductInventory productInventory;
	ProductMetadata productDetails;

	@Value("${server.storenum}")
	private int storeNum;

	@Autowired
	public CheckoutServiceImpl(ProductInventoryRepository productInventoryRepository, 
			ShoppingCartRestClient shoppingCartRestClient, ProductCatalogRestClient productCatalogRestClient) {
		this.productInventoryRepository = productInventoryRepository;
		this.shoppingCartRestClient = shoppingCartRestClient;
		this.productCatalogRestClient = productCatalogRestClient;	
	}

	@Autowired
	//private CassandraOperations cassandraTemplate;
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public Order checkout(String userId) throws NotEnoughProductsInStockException {
		Map<String, Integer> products = shoppingCartRestClient.getProductsInCart(userId);
		System.out.println("*** In Checkout products ***");
		StringBuilder updateCartpreparedStatement = new StringBuilder();
		updateCartpreparedStatement.append("BEGIN TRANSACTION");
		Order currentOrder = null;
//		StringBuilder orderDetails = new StringBuilder();
		JSONObject jsonObject = new JSONObject();
//		orderDetails.append("Customer bought these Items: ");
		JSONArray listOfProductsPurchased = new JSONArray();

		if (products.size() != 0) {
			for (Map.Entry<String, Integer> entry : products.entrySet()) {
				// Refresh quantity for every product before checking
				System.out.println("*** Checking out product *** " + entry.getKey());
				productInventory = productInventoryRepository.findById(entry.getKey()).orElse(null);
				productDetails = productCatalogRestClient.getProductDetails(entry.getKey());
				
				if (productInventory.getQuantity() < entry.getValue())
					throw new NotEnoughProductsInStockException(productDetails.getTitle(), productInventory.getQuantity());

				jdbcTemplate.execute(" UPDATE product_inventory SET quantity = quantity - " + entry.getValue() + " where sku = '" + entry.getKey() + "' ;");
//				orderDetails.append(" Product: " + productDetails.getTitle() + ", Quantity: " + entry.getValue() + ";");
				JSONObject productPurchasedObj = new JSONObject();
				productPurchasedObj.put("product",productDetails.getTitle());
				productPurchasedObj.put("quantity",entry.getValue());
				listOfProductsPurchased.put(productPurchasedObj);
			}
			double orderTotal = getTotal(products);
//			orderDetails.append(" Order Total is : " + orderTotal);
			jsonObject.put("order_total",orderTotal);
			jsonObject.put("products_purchased",listOfProductsPurchased);
//			currentOrder = createOrder(orderDetails.toString(), orderTotal);
			currentOrder = createOrder(jsonObject.toString().replaceAll("'","''"), orderTotal);

//			String tmpOrderDetails = currentOrder.getOrder_details();
//			if(tmpOrderDetails != null){
//				tmpOrderDetails = tmpOrderDetails.replaceAll("'","''");
//			}
			jdbcTemplate.execute(" INSERT INTO orders (order_id, user_id, order_details, order_time, order_total, store_num) VALUES ("
					+ "'" + currentOrder.getId() + "', " + "'1'" + ", '" + currentOrder.getOrder_details()
					+ "', '" + currentOrder.getOrder_time() + "'," + currentOrder.getOrder_total() + ","+storeNum+  ");");

			System.out.println("Statemet is " + updateCartpreparedStatement.toString());
		}
		products.clear();
		shoppingCartRestClient.clearCart(userId);
		System.out.println("*** Checkout complete, cart cleared ***");
		return currentOrder;

	}

	private Double getTotal(Map<String, Integer> products) {
		double price = 0.0;
		for (Map.Entry<String, Integer> entry : products.entrySet()) {

			productInventory = productInventoryRepository.findById(entry.getKey()).orElse(null);
			productDetails = productCatalogRestClient.getProductDetails(entry.getKey());
			price = price + productDetails.getPrice() * entry.getValue();
		}
		return price;
	}

	private Order createOrder(String orderDetails, double orderTotal) {
		Order order = new Order();
		LocalDateTime currentTime = LocalDateTime.now();
		order.setId(UUID.randomUUID().toString());
		order.setUser_id(1);
		order.setOrder_details(orderDetails);
		order.setOrder_time(currentTime.toString());
		order.setOrder_total(orderTotal);
		return order;
	}

}
