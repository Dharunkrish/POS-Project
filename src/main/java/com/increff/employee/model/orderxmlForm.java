package com.increff.employee.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class orderxmlForm {
    @XmlElement(name="order_id")
    private Integer order_id;
    @XmlElement(name="datetime")
    private String datetime;
    @XmlElement(name="total")
    private Double total;
    @XmlElement(name="item")
    private List<orderData> orderData;
	public Integer getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public List<orderData> getOrderInvoiceData() {
		return orderData;
	}
	public void setOrderInvoiceData(List<orderData> orderInvoiceData) {
		this.orderData = orderInvoiceData;
	}
}