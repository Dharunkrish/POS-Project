package com.increff.employee.model;


public class orderForm {

	private int id;
	private boolean isInvoiceGenerated;

	public boolean isInvoiceGenerated() {
		return isInvoiceGenerated;
	}
	public void setInvoiceGenerated(boolean isInvoiceGenerated) {
		this.isInvoiceGenerated = isInvoiceGenerated;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	private String time;





}
