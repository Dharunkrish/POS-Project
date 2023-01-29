package com.increff.employee.model;

public class reportData {
 
    private String date;
	private int total_order;
	private int total_item;
	private double revenue;
	

	public int getTotal_order() {
		return total_order;
	}
	public void setTotal_order(int total_order) {
		this.total_order = total_order;
	}
	public int getTotal_item() {
		return total_item;
	}
	public void setTotal_item(int total_item) {
		this.total_item = total_item;
	}
	public double getRevenue() {
		return revenue;
	}
	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}


}
