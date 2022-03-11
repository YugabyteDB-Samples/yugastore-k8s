package com.yugabyte.app.yugastore.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.yugabyte.app.yugastore.domain.ProductMetadata;
import com.yugabyte.app.yugastore.repo.ProductMetadataRepo;
import com.yugabyte.app.yugastore.service.ProductService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMetadataRepo productRepository;

    @Autowired
    public ProductServiceImpl(ProductMetadataRepo productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<ProductMetadata> findById(String id) {
        return productRepository.findById(id);
    }

	@Override
	public List<ProductMetadata> findAllProductsPageable(int limit, int offset) {
		
		return productRepository.getProducts(limit, offset);

	}

    @Override
    public int updateProduct(String sku, String title, String description, double price) {
        return productRepository.updateProduct(sku,title,description,price);
    }
}
