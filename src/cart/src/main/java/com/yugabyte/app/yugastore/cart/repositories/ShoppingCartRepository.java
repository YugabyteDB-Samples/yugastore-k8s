package com.yugabyte.app.yugastore.cart.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import com.yugabyte.app.yugastore.cart.domain.ShoppingCart;


@RepositoryRestResource
public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, String> {
	
	@Modifying
	@Transactional
	@Query("UPDATE shopping_cart SET quantity = quantity + 1 WHERE user_id = ?1 AND sku =?2")
	int updateQuantityForShoppingCart(String userId, String sku);
	
	@Query("SELECT quantity FROM shopping_cart WHERE user_id = ?1 AND sku = ?2")
	Optional<Integer> findByUserIdAndSku(String userId, String sku);
	
	@Query("SELECT sc FROM shopping_cart sc WHERE sc.userId = ?1")
	Optional<List<ShoppingCart>> findProductsInCartByUserId(String userId);

	@Modifying
	@Transactional
	@Query("UPDATE shopping_cart SET quantity = quantity - 1 WHERE user_id = ?1 AND sku =?2")
	int decrementQuantityForShoppingCart(String userId, String sku);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM shopping_cart WHERE user_id = ?1")
	int deleteProductsInCartByUserId(String userId);

}
