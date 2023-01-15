package com.increff.employee.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class productPojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int product_id;
	
	@Column(unique=true)
	private  String barcode;
	
	@ManyToOne
	private brandPojo brand;
	private String name;
	
	private double mrp;

	
	public String getBarcode() {
		return barcode;
	}
	
	public brandPojo getBrand() {
		return brand;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public void setBrand(brandPojo brand) {
		this.brand = brand;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMrp() {
		return mrp;
	}
	public void setMrp(double mrp) {
		this.mrp = mrp;
	}
}
