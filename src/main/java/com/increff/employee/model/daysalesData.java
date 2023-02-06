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

	private int brand_Category_id;

	
	public int getBrand_Category_id() {
		return brand_Category_id;
	}
	public void setBrand_Category_id(int brand_Category_id) {
		this.brand_Category_id = brand_Category_id;
	}
	public String getBarcode() {
		return barcode;
	}
	
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
 
	public daysalesData(String brand,String category,String barcode,int brand_category) {
		this.Brand=brand;
		this.Category=category;
		this.barcode=barcode;
		this.brand_Category_id=brand_category;
	}

}
