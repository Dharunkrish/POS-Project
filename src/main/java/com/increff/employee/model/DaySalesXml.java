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


	    
	}
