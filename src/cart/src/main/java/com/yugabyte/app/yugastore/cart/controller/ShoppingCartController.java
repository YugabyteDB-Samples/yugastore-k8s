package com.yugabyte.app.yugastore.cart.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yugabyte.app.yugastore.cart.service.ShoppingCartImpl;

@RestController
@RequestMapping(value = "/cart-microservice")
public class ShoppingCartController {
	
	@Autowired
	ShoppingCartImpl shoppingCart;
	
	@RequestMapping(method = RequestMethod.GET, value = "/shoppingCart/addProduct", produces = "application/json")
	public String addProductToCart(@RequestParam("userid") String userId, 
			@RequestParam("sku") String sku) {
		shoppingCart.addProductToShoppingCart(userId, sku);
		return String.format("Added to Cart");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/shoppingCart/productsInCart", produces = "application/json")
	public Map<String, Integer> getProductsInCart(@RequestParam("userid") String userId) {
		return shoppingCart.getProductsInCart(userId);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/shoppingCart/removeProduct", produces = "application/json")
	public String removeProductFromCart(@RequestParam("userid") String userId, 
			@RequestParam("sku") String sku) {
		shoppingCart.removeProductFromCart(userId, sku);
		return String.format("Removing from Cart");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/shoppingCart/clearCart", produces = "application/json")
	public String clearCart(@RequestParam("userid") String userId) {
		 shoppingCart.clearCart(userId);
		 return String.format("Clearing Cart, Checkout successful");
	}

}
