package com.increff.employee.model.Form;


public class productForm {

	private String name;
	private String barcode;
	private double mrp;
	public String getName() {
		return name;
	}

	

	private int brand_Category_id;

	public int getBrand_Category_id() {
		return brand_Category_id;
	}




	public void setBrand_Category_id(int brand_Category_id) {
		this.brand_Category_id = brand_Category_id;
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
