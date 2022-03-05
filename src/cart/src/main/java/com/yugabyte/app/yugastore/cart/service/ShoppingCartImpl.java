package com.yugabyte.app.yugastore.cart.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.yugabyte.app.yugastore.cart.domain.ShoppingCart;
import com.yugabyte.app.yugastore.cart.domain.ShoppingCartKey;
import com.yugabyte.app.yugastore.cart.repositories.ShoppingCartRepository;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class ShoppingCartImpl {
	
	private static final int DEFAULT_QUANTITY = 1;
	private final ShoppingCartRepository shoppingCartRepository;

	@Autowired
	public ShoppingCartImpl(ShoppingCartRepository shoppingCartRepository) {
		this.shoppingCartRepository = shoppingCartRepository;
	}

	/**
	 * If product is in the map just increment quantity by 1. If product is not in
	 * the map with, add it with quantity 1
	 *
	 * @param product
	 */
	public void addProductToShoppingCart(String userId, String sku) {

		ShoppingCartKey currentKey = new ShoppingCartKey(userId, sku);
		String shoppingCartKeyStr = userId + "-" + sku;
		if (shoppingCartRepository.findById(shoppingCartKeyStr).isPresent()) {
			shoppingCartRepository.updateQuantityForShoppingCart(userId, sku);
			System.out.println("Adding product: " + sku);
		} else {
			ShoppingCart currentShoppingCart = createCartObject(currentKey);
			shoppingCartRepository.save(currentShoppingCart);
			System.out.println("Adding product: " + sku);
		}
	}

	public Map<String, Integer> getProductsInCart(String userId) {

		Map<String, Integer> productsInCartSku = new HashMap<String, Integer>();

		if (shoppingCartRepository.findProductsInCartByUserId(userId).isPresent()) {

			List<ShoppingCart> productsInCart = shoppingCartRepository.findProductsInCartByUserId(userId).get();
			for (ShoppingCart item : productsInCart) {
				productsInCartSku.put(item.getSku(), item.getQuantity());
			}

		}
		return productsInCartSku;
	}

	public void removeProductFromCart(String userId, String sku) {
		String shoppingCartKeyStr = userId + "-" + sku;
		if (shoppingCartRepository.findById(shoppingCartKeyStr).isPresent()) {
			if (shoppingCartRepository.findById(shoppingCartKeyStr).get().getQuantity() > 1) {
				shoppingCartRepository.decrementQuantityForShoppingCart(userId, sku);
				System.out.println("Decrementing product: " + sku + " quantity");
			} else if (shoppingCartRepository.findById(shoppingCartKeyStr).get().getQuantity() == 1) {
				shoppingCartRepository.deleteById(shoppingCartKeyStr);
				System.out.println("Removing product: " + sku + " since it was qty 1");
			}
		}
	}

	private ShoppingCart createCartObject(ShoppingCartKey currentKey) {
		ShoppingCart currentShoppingCart = new ShoppingCart();
		currentShoppingCart.setCartKey(currentKey.getId() + "-" + currentKey.getSku());
		currentShoppingCart.setUserId(currentKey.getId());
		currentShoppingCart.setSku(currentKey.getSku());
		LocalDateTime currentTime = LocalDateTime.now();
		currentShoppingCart.setTime_added(currentTime.toString());
		currentShoppingCart.setQuantity(DEFAULT_QUANTITY);

		return currentShoppingCart;
	}

	public void clearCart(String userId) {

		if (shoppingCartRepository.findProductsInCartByUserId(userId).isPresent()) {
			shoppingCartRepository.deleteProductsInCartByUserId(userId);
			System.out.println("Deleteing all products for user: " + userId + " since checkout was successful");
		}
	}

}
