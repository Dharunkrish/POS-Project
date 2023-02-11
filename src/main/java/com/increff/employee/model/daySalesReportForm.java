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
    public daySalesReportForm(Double revenue,Long count){
    	if (count==null) {
    		this.count=0;
    	}
    	else {
    	this.count=(int)(long) count;
    	}
    	if (revenue==null) {
    		this.revenue=0;
    	}
    	else {
    	this.revenue=revenue;
    	}
    	
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
