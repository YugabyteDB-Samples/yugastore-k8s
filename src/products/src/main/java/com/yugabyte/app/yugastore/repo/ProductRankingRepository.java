package com.yugabyte.app.yugastore.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import com.yugabyte.app.yugastore.domain.ProductRanking;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
public interface ProductRankingRepository extends CrudRepository<ProductRanking, String> {
	
	@Query(nativeQuery = true, value="select pr.* from product_rankings pr where pr.sku=?1")
	@RestResource(path = "product", rel = "product")
	Optional<ProductRanking> findProductRankingById(String sku);
	
	@Query(nativeQuery = true, value="SELECT pr.* FROM product_rankings pr where category=?1 limit ?2 offset ?3")
	@RestResource(path = "category", rel = "category")
	public List<ProductRanking> getProductsByCategory(@Param("name") String category, @Param("limit") int limit, @Param("offset") int offset);

}
