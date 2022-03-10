package com.yugabyte.app.yugastore.cronoscheckoutapi.repositories;

import java.util.Optional;

import com.yugabyte.app.yugastore.cronoscheckoutapi.domain.ProductInventory;
import org.springframework.data.repository.CrudRepository;

public interface ProductInventoryRepository extends CrudRepository<ProductInventory, String> {
	Optional<ProductInventory> findById(String id);
}
