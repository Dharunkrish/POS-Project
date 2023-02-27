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

import com.increff.employee.model.Form.inventoryForm;
import com.increff.employee.model.Form.productForm;
import com.increff.employee.model.Form.orderForm;
import com.increff.employee.model.Form.orderitemForm;
import com.increff.employee.dao.orderitemDao;
import com.increff.employee.dao.reportDao;
import com.increff.employee.model.Form.reportForm;
import com.increff.employee.model.Xml.DaySalesXml;
import com.increff.employee.model.Xml.DaySalesXmlForm;
import com.increff.employee.model.Xml.InventoryReportXml;
import com.increff.employee.model.Xml.InventoryXmlForm;
import com.increff.employee.model.Xml.SalesReportDataXml;
import com.increff.employee.model.Xml.SalesXmlForm;
import com.increff.employee.model.Xml.orderData;
import com.increff.employee.model.Xml.orderxmlForm;
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.model.Data.productData;
import com.increff.employee.model.Data.brandData;
import com.increff.employee.model.Form.brandForm;
import com.increff.employee.model.Form.daySalesReportForm;





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
    

		public brandData BrandInitialise1() throws ApiException {
			brandForm p = new brandForm();
			p.setBrand("nestle1");
			p.setCategory("maggi1");
			brandservice.add(p);
			return brandservice.getAll().get(0);
		}
	
		public brandData BrandInitialise2() throws ApiException {
			brandForm p1 = new brandForm();
			p1.setBrand("bru1");
			p1.setCategory("coffee1");
			brandservice.add(p1);
			return brandservice.getAll().get(1);
		}

	public productData prodInitialise1() throws Exception{ 
		brandData brand=BrandInitialise1();
		productForm p = new productForm();
        p.setName("150 g");
		p.setBarcode("1");
		p.setMrp(1000.0);
		p.setBrand_Category_id(brand.getId());
		prodservice.add(p);
		return prodservice.getAll().get(0);
	}

	public  productData prodInitialise2()  throws Exception {
		brandData brand=BrandInitialise2();
		productForm p = new productForm();
        p.setName("1 Liter");
		p.setBarcode("12");
		p.setMrp(2000.0);
		p.setBrand_Category_id(brand.getId());
		prodservice.add(p);
		return prodservice.getAll().get(1);
	}

	public inventoryForm InvInitialise1()  throws Exception {
		productData p=prodInitialise1();
		inventoryForm i = new inventoryForm();
		i.setId(p.getProduct_id());
        i.setName("150 g");
		i.setBarcode("1");
		i.setQuantity(10);
		invservice.add(i);
		return i;
	}

	public inventoryForm InvInitialise2()  throws Exception {
	    productData p=prodInitialise2();
		inventoryForm i = new inventoryForm();
        i.setId(p.getProduct_id());
        i.setName("1 Liter");
		i.setBarcode("12");
		i.setQuantity(20);
		invservice.add(i);
		return i;
	}

	public orderitemForm OrderItemInitialise1() throws Exception{
		inventoryForm i1= InvInitialise1();
		orderitemForm oi=new orderitemForm();
		oi.setName(i1.getName());
		oi.setBarcode(i1.getBarcode());
		oi.setQuantity(10);
		oi.setPrice(100);
		return oi;
	}

	public orderitemForm OrderItemInitialise2() throws Exception{
		inventoryForm i1= InvInitialise2();
		orderitemForm oi=new orderitemForm();
		oi.setName(i1.getName());
		oi.setBarcode(i1.getBarcode());
		oi.setQuantity(5);
		oi.setPrice(200);
		return oi;
	}


	public orderForm Initialise1()  throws Exception {
		orderitemForm oi1=OrderItemInitialise1();
		orderitemForm oi2=OrderItemInitialise2();
		List<orderitemForm> o=new ArrayList<orderitemForm> ();
		o.add(oi1);
		o.add(oi2);
		orderservice.AddItems(o);
		orderForm of=orderservice.getAll().get(0);
		orderPojo o1=new orderPojo();
		o1.setId(of.getId());
		System.err.print(of.isInvoiceGenerated());
		o1.setInvoiceGenerated(of.isInvoiceGenerated());
        o1.setT(ZonedDateTime.now().minus(Period.ofDays(1)));
		dao.update(o1);
		System.err.print(o1.isInvoiceGenerated());
		return orderservice.getAll().get(0);
	}

	public orderForm Initialise2()  throws Exception {
		orderPojo order =new orderPojo();
		order.setInvoiceGenerated(false);
		order=dao.create(order);
		orderitemForm oi1=OrderItemInitialise1();
		orderitemForm oi2=OrderItemInitialise2();
		oi1.setQuantity(oi1.getQuantity()-2);
		oi2.setQuantity(oi2.getQuantity()-4);
		List<orderitemForm> o=new ArrayList<orderitemForm> ();
		o.add(oi1);
		o.add(oi2);
		orderservice.AddItems(o);
		orderForm of=orderservice.getAll().get(0);
		orderPojo o1=new orderPojo();
		o1.setId(of.getId());
		o1.setInvoiceGenerated(of.isInvoiceGenerated());
		orderservice.update(o1.getId(), o1);
		return orderservice.getAll().get(0);
	}

	@Test
	public void TestGenerateInvoiceXMLList() throws Exception{
		   orderForm o=Initialise1();
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
		   orderPojo o1=new orderPojo();
		   o1=orderservice.getorder(o.getId());
		   assertEquals((Integer)o.getId(), xmlList.getOrder_id());
		   assertEquals(o1.getT().format(formatter), xmlList.getDatetime());
		   assertEquals(2000, xmlList.getTotal(),0.01);
		   assertEquals(true, o1.isInvoiceGenerated());
	}

	@Test
	public void TestcalculateTotal() throws Exception{
		orderForm o=Initialise1();
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
		System.err.print(orderservice.getAll().get(0).getTime());
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

	