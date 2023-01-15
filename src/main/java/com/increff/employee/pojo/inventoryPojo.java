package com.increff.employee.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;


@Entity
public class inventoryPojo {

	@Id
	private int id;
	private int quantity;

	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	public inventoryPojo(int quantity) {
		this.quantity=quantity;
	}
	
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public inventoryPojo() {
	}

}
