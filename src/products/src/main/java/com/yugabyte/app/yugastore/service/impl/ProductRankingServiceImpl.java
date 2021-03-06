package com.yugabyte.app.yugastore.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.yugabyte.app.yugastore.domain.ProductRanking;
import com.yugabyte.app.yugastore.repo.ProductRankingRepository;
import com.yugabyte.app.yugastore.service.ProductRankingService;
import org.springframework.web.context.WebApplicationContext;

@Service
public class ProductRankingServiceImpl implements ProductRankingService {
	
	private final ProductRankingRepository productRankingRepository;
	
	@Autowired
	public ProductRankingServiceImpl(ProductRankingRepository productRankingRepository) {
		this.productRankingRepository = productRankingRepository;
	}

	@Override
	public Optional<ProductRanking> findProductRankingById(String sku) {
		
		return productRankingRepository.findProductRankingById(sku);
	}

	@Override
	public List<ProductRanking> getProductsByCategory(String category, int limit, int offset) {
		
		return productRankingRepository.getProductsByCategory(category, limit, offset);
	}

}
