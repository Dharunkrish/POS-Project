package com.increff.employee.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class SalesXmlForm {

	@XmlElement
	private String from;
	
	@XmlElement
	private String to;
	
	@XmlElement
	private String brand;
	
	@XmlElement
	private String category;
	
	
	public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}


	public String getTo() {
		return to;
	}


	public void setTo(String to) {
		this.to = to;
	}


	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public List<SalesReportDataXml> getData() {
		return data;
	}


	public void setData(List<SalesReportDataXml> data) {
		this.data = data;
	}


	@XmlElement
	private List<SalesReportDataXml> data;
}
