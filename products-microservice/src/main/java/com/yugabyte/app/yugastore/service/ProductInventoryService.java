package com.yugabyte.app.yugastore.service;

import java.util.Optional;

import com.yugabyte.app.yugastore.domain.*;
import org.springframework.stereotype.Service;

public interface ProductInventoryService {

    Optional<ProductInventory> findById(String id);

}
