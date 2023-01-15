package com.increff.employee.model;

public class productDTO {

	private String name;
	private String barcode;
	private double mrp;
	private int brand_id;
	private int product_id;
	
	public productDTO(String name,String barcode,double mrp,int brand_id,int product_id) {
		this.barcode=barcode;
		this.name=name;
		this.mrp=mrp;
		this.brand_id=brand_id;
		this.product_id=product_id;
	}
	
	public productDTO(String name,int product_id) {
		this.name=name;
		this.product_id=product_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public double getMrp() {
		return mrp;
	}

	public void setMrp(double mrp) {
		this.mrp = mrp;
	}

	public int getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
}
