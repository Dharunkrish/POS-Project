package com.increff.employee.model.Form;


public class orderitemForm {

	private String barcode;
	private int quantity;
	private double price;
	private String name;
	private int old_q;

	public int getOld_q() {
		return old_q;
	}
	public void setOld_q(int old_q) {
		this.old_q = old_q;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}



}
