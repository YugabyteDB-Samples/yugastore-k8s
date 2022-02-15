package com.yugabyte.app.yugastore.domain;

import java.io.Serializable;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class ProductRankingKey implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6646128061564873843L;

	@PrimaryKeyColumn(name = "sku", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String sku;
	
	@PrimaryKeyColumn(name = "category", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private String category;

	public String getSku() {
		return sku;
	}

	public String getCategory() {
		return category;
	}

	public void setId(String sku) {
		this.sku = sku;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((sku == null) ? 0 : sku.hashCode());
	    result = prime * result + ((sku == null) ? 0 : sku.hashCode());
	    return result;
	  }
	  
	  @Override
	  public boolean equals(Object obj) {
	    if (this == obj)
	      return true;
	    if (obj == null)
	      return false;
	    if (getClass() != obj.getClass())
	      return false;
	    ProductRankingKey other = (ProductRankingKey) obj;
	    if (sku == null) {
	      if (other.sku != null)
	        return false;
	      else {
	    	  if (!sku.equals(other.sku)) {
	    		  return false;
	    	  }
	      }
	    }
	    return true;
	  }

}
