package com.increff.employee.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class DaySalesXml {




	@XmlElement
	private String from;
	@XmlElement
	private String to;
	@XmlElement
	private List<DaySalesXmlForm> data;
	@XmlElement
	private int total_order;
	@XmlElement
	private int total_item;
	@XmlElement
	private double revenue;

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
	public List<DaySalesXmlForm> getData() {
		return data;
	}
	public void setData(List<DaySalesXmlForm> data) {
		this.data = data;
	}

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

	    
	}
