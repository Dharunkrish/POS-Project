package com.increff.employee.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryXmlForm {

			@XmlElement
			private int quantity;
			@XmlElement
			private String category;

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



				public String getBrand() {
					return brand;
				}


				public void setBrand(String brand) {
					this.brand = brand;
				}


	}
