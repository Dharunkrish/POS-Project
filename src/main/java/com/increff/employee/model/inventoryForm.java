package com.increff.employee.model;

public class inventoryForm {

	private int id;
	private int quantity;
	private int prev_id;
	public int getPrev_id() {
		return prev_id;
	}
	public void setPrev_id(int prev_id) {
		this.prev_id = prev_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
