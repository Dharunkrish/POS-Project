package com.increff.employee.pojo;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class orderPojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
    private ZonedDateTime t;
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
	public ZonedDateTime getT() {
		return t;
	}
	public void setT(ZonedDateTime t) {
		this.t = t;
	}
	


}
