package com.increff.employee.service;

import static org.junit.Assert.assertEquals;
import org.junit.rules.ExpectedException;
import org.junit.Rule;

import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;




import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.dao.orderitemDao;
import com.increff.employee.dao.reportDao;
import com.increff.employee.model.orderData;
import com.increff.employee.model.orderxmlForm;
import com.increff.employee.model.reportForm;
import com.increff.employee.pojo.brandPojo;
import com.increff.employee.pojo.daySalesReportPojo;





public class InvoiceServiceTest extends AbstractUnitTest {

	@Autowired
	private invoiceService service;

	@Autowired
	private orderitemService orderservice;

	@Autowired
	private InventoryService invservice;

	@Autowired
	private productService prodservice;

	@Autowired
	private brandService brandservice;

	@Autowired
	private orderitemDao dao;



	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");



	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
    

		public brandPojo BrandInitialise1() throws ApiException {
			brandPojo p = new brandPojo();
			p.setBrand("nestle1");
			p.setCategory("maggi1");
			brandservice.add(p);
			return p;
		}
	
		public brandPojo BrandInitialise2() throws ApiException {
			brandPojo p1 = new brandPojo();
			p1.setBrand("bru1");
			p1.setCategory("coffee1");
			brandservice.add(p1);
			return p1;
		}

	public productPojo prodInitialise1() throws ApiException{ 
		brandPojo brand=BrandInitialise1();
		productPojo p = new productPojo();
        p.setName("150 g");
		p.setBarcode("1");
		p.setMrp(1000.0);
		p.setBrand_Category_id(brand.getId());
		prodservice.add(p);
		return p;
	}

	public  productPojo prodInitialise2()  throws ApiException {
		brandPojo brand=BrandInitialise2();
		productPojo p = new productPojo();
        p.setName("1 Liter");
		p.setBarcode("12");
		p.setMrp(2000.0);
		p.setBrand_Category_id(brand.getId());
		prodservice.add(p);
		return p;
	}

	public inventoryPojo InvInitialise1()  throws ApiException {
		productPojo p=prodInitialise1();
		inventoryPojo i = new inventoryPojo();
		i.setId(p.getProduct_id());
        i.setName("150 g");
		i.setBarcode("1");
		i.setQuantity(10);
		invservice.add(i);
		return i;
	}

	public inventoryPojo InvInitialise2()  throws ApiException {
	    productPojo p=prodInitialise2();
		inventoryPojo i = new inventoryPojo();
        i.setId(p.getProduct_id());
        i.setName("1 Liter");
		i.setBarcode("12");
		i.setQuantity(20);
		invservice.add(i);
		return i;
	}

	public orderitemPojo OrderItemInitialise1() throws ApiException{
		inventoryPojo i1= InvInitialise1();
		orderitemPojo oi=new orderitemPojo();
		oi.setName(i1.getName());
		oi.setBarcode(i1.getBarcode());
		oi.setQuantity(10);
		oi.setPrice(100);
		return oi;
	}

	public orderitemPojo OrderItemInitialise2() throws ApiException{
		inventoryPojo i1= InvInitialise2();
		orderitemPojo oi=new orderitemPojo();
		oi.setName(i1.getName());
		oi.setBarcode(i1.getBarcode());
		oi.setQuantity(5);
		oi.setPrice(200);
		return oi;
	}


	public orderPojo Initialise1()  throws ApiException {
		orderPojo order =new orderPojo();
		order.setT(ZonedDateTime.now().minus(Period.ofDays(1)));
		order.setInvoiceGenerated(false);
		order=dao.create(order);
		orderitemPojo oi1=OrderItemInitialise1();
		orderitemPojo oi2=OrderItemInitialise2();
		orderservice.AddSingleItem(oi1,order.getId());
		orderservice.AddSingleItem(oi2,order.getId());
		return order;
	}

	@Test
	public void TestGenerateXMLList() throws Exception{
		   orderPojo o=Initialise1();
		   orderxmlForm xmlList=new orderxmlForm();
		   xmlList.setOrder_id(o.getId());
		   List<orderData> od=new ArrayList<orderData>();
		   for(orderitemPojo oi:orderservice.get(o.getId())){
                  orderData d=new orderData();
				  d.setName(oi.getName());
				  d.setQuantity(oi.getQuantity());
				  d.setMrp(oi.getPrice());
				  d.setPrice(oi.getQuantity()*oi.getPrice());
				  od.add(d);
		   }
		   xmlList.setOrderInvoiceData(od);
		   assertEquals(false, o.isInvoiceGenerated());
		   xmlList=service.generateInvoiceList(xmlList);
		   o=orderservice.getorder(o.getId());
		   assertEquals((Integer)o.getId(), xmlList.getOrder_id());
		   assertEquals(o.getT().format(formatter), xmlList.getDatetime());
		   assertEquals(2000, xmlList.getTotal(),0.01);
		   assertEquals(true, o.isInvoiceGenerated());
	}

	@Test
	public void TestcalculateTotal() throws Exception{
		orderPojo o=Initialise1();
		orderxmlForm xmlList=new orderxmlForm();
		xmlList.setOrder_id(o.getId());
		List<orderData> od=new ArrayList<orderData>();
		for(orderitemPojo oi:orderservice.get(o.getId())){
			   orderData d=new orderData();
			   d.setName(oi.getName());
			   d.setQuantity(oi.getQuantity());
			   d.setMrp(oi.getPrice());
			   d.setPrice(oi.getQuantity()*oi.getPrice());
			   od.add(d);
		}
		xmlList.setOrderInvoiceData(od);
		double total=invoiceService.calculateTotal(xmlList);
		assertEquals(2000,total,0.01);
	}
 
	}

	