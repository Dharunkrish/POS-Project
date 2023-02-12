package com.increff.employee.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;


@Entity
public class inventoryPojo {

	@Id
	@Column(nullable=false)
	private int id;
	@Column(nullable=false)
	private int quantity;
	private String barcode;
	private String name;

	
	public String getBarcode() {
		return barcode;
	}


	public void setBarcode(String barocode) {
		this.barcode = barocode;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


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
