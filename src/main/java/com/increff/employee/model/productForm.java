package com.increff.employee.model;


public class productForm {

	private String name;
	private String barcode;
	private double mrp;
	private String brand_Category;
	public String getName() {
		return name;
	}

	public String getBrand_Category() {
		return brand_Category;
	}



	public void setBrand_Category(String brand_Category) {
		this.brand_Category = brand_Category;
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


}
