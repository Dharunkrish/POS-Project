package com.increff.employee.model.Form;

import java.time.ZonedDateTime;

public class orderForm {

	private int id;
	private boolean isInvoiceGenerated;
	private ZonedDateTime t;

	public ZonedDateTime getT(){
		return t;
	}

	public void setT(ZonedDateTime t){
		this.t=t;
	}
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
