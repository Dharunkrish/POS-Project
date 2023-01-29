package com.increff.employee.model;

public class daysalesData {
	
    private String Brand;
    private String Category;
	

	public String getBrand() {
		return Brand;
	}
	public void setBrand(String brand) {
		Brand = brand;
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
   
	private String barcode;

	private String brand_category;

	public String getBrand_category() {
		return brand_category;
	}
	public void setBrand_category(String brand_category) {
		this.brand_category = brand_category;
	}
	public String getBarcode() {
		return barcode;
	}
	
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
 
	public daysalesData(String brand,String category,String barcode,String brand_category) {
		this.Brand=brand;
		this.Category=category;
		this.barcode=barcode;
		this.brand_category=brand_category;
	}

}
