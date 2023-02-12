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
	
	@Column(unique=true,nullable=false)
	private  String barcode;
	 
	@Column(nullable=false)
	private int brand_Category_id;
	
	public int getBrand_Category_id() {
		return brand_Category_id;
	}


	public void setBrand_Category_id(int brand_Category_id) {
		this.brand_Category_id = brand_Category_id;
	}
	private String name;
	
	@Column(nullable=false)
	private double mrp;

	
	public String getBarcode() {
		return barcode;
	}
	

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
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
