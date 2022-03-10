package com.yugabyte.app.yugastore.service;

import java.util.List;
import java.util.Optional;

import com.yugabyte.app.yugastore.domain.*;
import org.springframework.stereotype.Service;

public interface ProductService {

    Optional<ProductMetadata> findById(String id);

    List<ProductMetadata> findAllProductsPageable(int limit, int offset);

}
