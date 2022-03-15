package com.yugabyte.app.yugastore.repo;

import java.util.List;
import java.util.Optional;

import com.yugabyte.app.yugastore.domain.OrderCount;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.yugabyte.app.yugastore.domain.ProductMetadata;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(path = "product")
public interface ProductMetadataRepo extends CrudRepository<ProductMetadata, String> {
	
	//@Query("SELECT * FROM products limit ?0 offset ?1")
	@Query(nativeQuery = true, value="SELECT p.* FROM products p limit ?1 offset ?2")
	@RestResource(path = "products", rel = "products")
	public List<ProductMetadata> getProducts(@Param("limit") int limit, @Param("offset") int offset);

	Optional<ProductMetadata> findById(String id);

	@Modifying
	@Transactional
	@Query("update products p set p.title = ?2, p.description = ?3, p.price = ?4 where p.id = ?1")
	int updateProduct(String sku, String title, String description, double price);

	@Query(nativeQuery = true, value="SELECT count(*) as ordercount FROM orders where store_num = ?1")
	@RestResource(path = "orders", rel = "orders")
    int getOrderCount(int storeNum);
}
