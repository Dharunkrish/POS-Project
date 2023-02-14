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
import com.increff.employee.model.DaySalesXml;
import com.increff.employee.model.DaySalesXmlForm;
import com.increff.employee.model.InventoryReportXml;
import com.increff.employee.model.InventoryXmlForm;
import com.increff.employee.model.SalesReportDataXml;
import com.increff.employee.model.SalesXmlForm;
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
	private reportService repservice;

	@Autowired
	private productService prodservice;

	@Autowired
	private brandService brandservice;

	@Autowired
	private orderitemDao dao;



	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");




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

	public orderPojo Initialise2()  throws ApiException {
		orderPojo order =new orderPojo();
		order.setT(ZonedDateTime.now().minus(Period.ofDays(1)));
		order.setInvoiceGenerated(false);
		order=dao.create(order);
		orderitemPojo oi1=OrderItemInitialise1();
		orderitemPojo oi2=OrderItemInitialise2();
		oi1.setQuantity(oi1.getQuantity()-2);
		oi2.setQuantity(oi2.getQuantity()-4);
		orderservice.AddSingleItem(oi1,order.getId());
		orderservice.AddSingleItem(oi2,order.getId());
		return order;
	}

	@Test
	public void TestGenerateInvoiceXMLList() throws Exception{
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

	@Test
	public void TestGenerateDaySalesXMLList() throws Exception{
		Initialise1();
		repservice.add();
		reportForm s=new reportForm();
		s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
		s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		DaySalesXml d=service.generateDaySalesReport(s);
		assertEquals(s.getFrom().format(formatter1),d.getFrom());
		assertEquals(s.getTo().format(formatter1), d.getTo());
		List<DaySalesXmlForm> ds=d.getData();
		assertEquals(1, ds.get(0).getTotal_order());
		assertEquals(2, ds.get(0).getTotal_item());
		assertEquals(2000, ds.get(0).getRevenue(),0.01);
		assertEquals(ZonedDateTime.now().minus(Period.ofDays(1)).format(formatter1), ds.get(0).getDate());
	}

	@Test
	public void TestGenerateSalesXMLList() throws Exception{
		Initialise1();
		reportForm s=new reportForm();
		s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
		s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		s.setBrand("none");
		s.setCategory("none");
		SalesXmlForm d=service.generateSalesReport(s);
		assertEquals(s.getFrom().format(formatter1),d.getFrom());
		assertEquals(s.getTo().format(formatter1), d.getTo());
		assertEquals("All",d.getBrand());
		assertEquals("All", d.getCategory());
		List<SalesReportDataXml> ds=d.getData();
		SalesReportDataXml o=ds.get(0);
		assertEquals("nestle1", o.getBrand());
		assertEquals("maggi1", o.getCategory());
		assertEquals(10, o.getQuantity());
		assertEquals(1000.0, o.getRevenue(),0.01);
		o=ds.get(1);
		assertEquals("bru1", o.getBrand());
		assertEquals("coffee1", o.getCategory());
		assertEquals(5, o.getQuantity());
		assertEquals(1000.0, o.getRevenue(),0.01);
		assertEquals(15,d.getTotal_quantity());
		assertEquals(2000, d.getRevenue(),0.01);
	}

	@Test
	public void TestGenerateSalesXMLListWithBrand() throws Exception{
		Initialise1();
		reportForm s=new reportForm();
		s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
		s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		s.setBrand("nestle1");
		s.setCategory("none");
		SalesXmlForm d=service.generateSalesReport(s);
		assertEquals(s.getFrom().format(formatter1),d.getFrom());
		assertEquals(s.getTo().format(formatter1), d.getTo());
		assertEquals("nestle1",d.getBrand());
		assertEquals("All", d.getCategory());
		List<SalesReportDataXml> ds=d.getData();
		SalesReportDataXml o=ds.get(0);
		assertEquals("nestle1", o.getBrand());
		assertEquals("maggi1", o.getCategory());
		assertEquals(10, o.getQuantity());
		assertEquals(1000.0, o.getRevenue(),0.01);
		assertEquals(10,d.getTotal_quantity());
		assertEquals(1000, d.getRevenue(),0.01);
	}

	@Test
	public void TestGenerateSalesXMLListWithCategory() throws Exception{
		Initialise1();
		reportForm s=new reportForm();
		s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
		s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		s.setBrand("none");
		s.setCategory("coffee1");
		SalesXmlForm d=service.generateSalesReport(s);
		assertEquals(s.getFrom().format(formatter1),d.getFrom());
		assertEquals(s.getTo().format(formatter1), d.getTo());
		assertEquals("All",d.getBrand());
		assertEquals("coffee1", d.getCategory());
		List<SalesReportDataXml> ds=d.getData();
		SalesReportDataXml o=ds.get(0);
		assertEquals("bru1", o.getBrand());
		assertEquals("coffee1", o.getCategory());
		assertEquals(5, o.getQuantity());
		assertEquals(1000.0, o.getRevenue(),0.01);
		assertEquals(5,d.getTotal_quantity());
		assertEquals(1000, d.getRevenue(),0.01);
	}

	@Test
	public void TestGenerateSalesXMLListWithBrandCategory() throws Exception{
		Initialise1();
		reportForm s=new reportForm();
		s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
		s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		s.setBrand("bru1");
		s.setCategory("coffee1");
		SalesXmlForm d=service.generateSalesReport(s);
		assertEquals(s.getFrom().format(formatter1),d.getFrom());
		assertEquals(s.getTo().format(formatter1), d.getTo());
		assertEquals("bru1",d.getBrand());
		assertEquals("coffee1", d.getCategory());
		List<SalesReportDataXml> ds=d.getData();
		SalesReportDataXml o=ds.get(0);
		assertEquals("bru1", o.getBrand());
		assertEquals("coffee1", o.getCategory());
		assertEquals(5, o.getQuantity());
		assertEquals(1000.0, o.getRevenue(),0.01);
		assertEquals(5,d.getTotal_quantity());
		assertEquals(1000, d.getRevenue(),0.01);
	}
	@Test
	public void TestGenerateInventoryXMLList() throws Exception{
		Initialise2();
		InventoryReportXml d=service.generateInventoryReport();
		List<InventoryXmlForm> ds=d.getData();
		InventoryXmlForm o=ds.get(0);
		assertEquals("nestle1", o.getBrand());
		assertEquals("maggi1", o.getCategory());
		assertEquals(2, o.getQuantity());
		o=ds.get(1);
		assertEquals("bru1", o.getBrand());
		assertEquals("coffee1", o.getCategory());
		assertEquals(19, o.getQuantity());
		assertEquals(21,d.getTotal_quantity());
	}

	@Test
	public void TestInventoryTotal() throws Exception{
		Initialise2();
		InventoryReportXml d=service.generateInventoryReport();
		int total=invoiceService.calculateInventoryTotal(d.getData());
		assertEquals(21,total);
	}

	@Test
	public void TestSalesReportTotal() throws Exception{
	   Initialise1();
		reportForm s=new reportForm();
		s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
		s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		s.setBrand("none");
		s.setCategory("none");
		SalesXmlForm d=service.generateSalesReport(s);
		List<Object> m=invoiceService.calculateSalesTotal(d.getData());
		assertEquals(15,m.get(0));
		assertEquals(2000.0, m.get(1));
	}

	@Test
	public void TestDaySalesReportTotal() throws Exception{
	    Initialise1();
		repservice.add();
		reportForm s=new reportForm();
		s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
		s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		DaySalesXml d=service.generateDaySalesReport(s);
		List<Object> m=service.calculateDaySalesTotal(d.getData());
		System.err.print(d.getData().get(0).getDate());
		assertEquals(1, m.get(0));
		assertEquals(2, m.get(2));
		assertEquals(2000.0,(double)m.get(1),0.01);
	}
	}

	