package com.increff.employee.model;


public class daySalesReportForm {

	private double revenue;
	private int count;
	public double getRevenue() {
		return revenue;
	}
	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
    public daySalesReportForm(double revenue,long count){
    	this.count=(int) count;
    	this.revenue=revenue;
    }
    
    public daySalesReportForm(){
    }
    
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
   

}
