package com.increff.employee.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class DaySalesXmlForm {




	@XmlElement
	private int total_order;
	@XmlElement
	private int total_item;
	@XmlElement
	private double revenue;


	    @XmlElement
	    private String Date;


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
			return Date;
		}


		public void setDate(String date) {
			Date = date;
		}
	    
	}
