package com.yugabyte.app.yugastore.cart.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ShoppingCartKey implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 343234890377423559L;

	@Column(name = "user_id")
	private String id;

	@Column(name = "sku")
	private String sku;
	
	public ShoppingCartKey() {
		
	}
	
	public ShoppingCartKey(String id, String sku) {
		this.id = id;
		this.sku = sku;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : sku.hashCode());
		result = prime * result + ((sku == null) ? 0 : id.hashCode());
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
		ShoppingCartKey other = (ShoppingCartKey) obj;
		if (id == null) {
			if (other.id != null)
				return false;
			else {
				if (!id.equals(other.id)) {
					return false;
				}
			}
		}
		return true;
	}
}
