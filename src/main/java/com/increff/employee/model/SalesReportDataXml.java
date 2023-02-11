package com.increff.employee.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SalesReportDataXml {

		@XmlElement
		private int quantity;
		@XmlElement
		private String category;
		@XmlElement
		private double revenue;


		    @XmlElement
		    private String brand;


			public int getQuantity() {
				return quantity;
			}


			public void setQuantity(int quantity) {
				this.quantity = quantity;
			}


			public String getCategory() {
				return category;
			}


			public void setCategory(String category) {
				this.category = category;
			}


			public double getRevenue() {
				return revenue;
			}


			public void setRevenue(double revenue) {
				this.revenue = revenue;
			}


			public String getBrand() {
				return brand;
			}


			public void setBrand(String brand) {
				this.brand = brand;
			}


}
