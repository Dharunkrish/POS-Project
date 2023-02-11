package com.increff.employee.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryReportXml {

	public List<InventoryXmlForm> getData() {
		return data;
	}

	public void setData(List<InventoryXmlForm> data) {
		this.data = data;
	}

	@XmlElement
	private List<InventoryXmlForm> data;
}
