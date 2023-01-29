package com.increff.employee.pojo;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "pos_day_sales")

public class daySalesReportPojo {
	
	@Id
	private ZonedDateTime date;
	private int total_orders;
	private int total_items;
	private double revenue;
	public ZonedDateTime getDate() {
		return date;
	}
	public void setDate(ZonedDateTime date) {
		this.date = date;
	}
	public int getTotal_orders() {
		return total_orders;
	}
	public void setTotal_orders(int total_orders) {
		this.total_orders = total_orders;
	}
	public int getTotal_items() {
		return total_items;
	}
	public void setTotal_items(int total_items) {
		this.total_items = total_items;
	}
	public double getRevenue() {
		return revenue;
	}
	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}
}