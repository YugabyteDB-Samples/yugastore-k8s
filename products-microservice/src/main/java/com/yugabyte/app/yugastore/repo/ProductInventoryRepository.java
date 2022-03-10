package com.yugabyte.app.yugastore.repo;

import java.util.Optional;

//import org.springframework.data.cassandra.repository.CassandraRepository;

import com.yugabyte.app.yugastore.domain.ProductInventory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
public interface ProductInventoryRepository extends CrudRepository<ProductInventory, String> {
	Optional<ProductInventory> findById(String id);
}
