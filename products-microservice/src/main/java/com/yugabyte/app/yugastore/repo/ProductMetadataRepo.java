package com.yugabyte.app.yugastore.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.yugabyte.app.yugastore.domain.ProductMetadata;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "product")
public interface ProductMetadataRepo extends CrudRepository<ProductMetadata, String> {
	
	//@Query("SELECT * FROM products limit ?0 offset ?1")
	@Query(nativeQuery = true, value="SELECT p.* FROM products p limit ?1 offset ?2")
	@RestResource(path = "products", rel = "products")
	public List<ProductMetadata> getProducts(@Param("limit") int limit, @Param("offset") int offset);

	Optional<ProductMetadata> findById(String id);
}
