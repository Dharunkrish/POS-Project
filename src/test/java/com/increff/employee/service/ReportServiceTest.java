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
import java.time.LocalDate;
import java.time.LocalTime;




import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.dao.brandDao;
import com.increff.employee.dao.inventoryDao;
import com.increff.employee.dao.orderitemDao;
import com.increff.employee.dao.reportDao;
import com.increff.employee.model.daysalesData;
import com.increff.employee.model.reportForm;
import com.increff.employee.pojo.brandPojo;
import com.increff.employee.pojo.daySalesReportPojo;





public class ReportServiceTest extends AbstractUnitTest {

	@Autowired
	private reportService service;

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

	@Autowired
	private inventoryDao idao;

	@Autowired
	private brandDao bdao;


	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");



	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
    
		public brandPojo BrandInitialise1() throws ApiException {
			brandPojo p = new brandPojo();
			p.setBrand("nestle");
			p.setCategory("tea");
			brandservice.add(p);
			return p;
		}
	
		public brandPojo BrandInitialise2() throws ApiException {
			brandPojo p1 = new brandPojo();
			p1.setBrand("bru");
			p1.setCategory("coffee");
			brandservice.add(p1);
			return p1;
		}

		public brandPojo BrandInitialise3() throws ApiException {
			brandPojo p = new brandPojo();
			p.setBrand("bru");
			p.setCategory("tea");
			brandservice.add(p);
			return p;
		}
	
		public brandPojo BrandInitialise4() throws ApiException {
			brandPojo p1 = new brandPojo();
			p1.setBrand("nestle");
			p1.setCategory("coffee");
			brandservice.add(p1);
			return p1;
		}
        

	public productPojo prodInitialise1() throws ApiException{ 
		brandPojo brand=BrandInitialise1();
		productPojo p = new productPojo();
        p.setName("150 g");
		p.setBarcode("1");
		p.setMrp(100.0);
		p.setBrand_Category_id(brand.getId());
		prodservice.add(p);
		return p;
	}

	public  productPojo prodInitialise2()  throws ApiException {
		brandPojo brand=BrandInitialise3();
		productPojo p = new productPojo();
        p.setName("250 g");
		p.setBarcode("12");
		p.setMrp(120.0);
		p.setBrand_Category_id(brand.getId());
		prodservice.add(p);
		return p;
	}

	public  productPojo prodInitialise3()  throws ApiException {
		brandPojo b=bdao.selectProduct("bru", "tea");
		productPojo p = new productPojo();
        p.setName("500 g");
		p.setBarcode("123");
		p.setMrp(100.0);
		p.setBrand_Category_id(b.getId());
		prodservice.add(p);
		return p;
	}

	public  productPojo prodInitialise4()  throws ApiException {
		brandPojo brand=BrandInitialise4();
		productPojo p = new productPojo();
        p.setName("1 Kg");
		p.setBarcode("1234");
		p.setMrp(200.0);
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
        i.setName("250 g");
		i.setBarcode("12");
		i.setQuantity(20);
		invservice.add(i);
		return i;
	}

	public inventoryPojo InvInitialise3()  throws ApiException {
	    productPojo p=prodInitialise3();
		inventoryPojo i = new inventoryPojo();
        i.setId(p.getProduct_id());
        i.setName("500g");
		i.setBarcode("123");
		i.setQuantity(10);
		invservice.add(i);
		return i;
	}

	public inventoryPojo InvInitialise4()  throws ApiException {
	    productPojo p=prodInitialise4();
		inventoryPojo i = new inventoryPojo();
        i.setId(p.getProduct_id());
        i.setName("1 Kg");
		i.setBarcode("1234");
		i.setQuantity(20);
		invservice.add(i);
		return i;
	}

	public void InvInitialise()  throws ApiException {
	    InvInitialise1();
		InvInitialise2();
		InvInitialise3();
		InvInitialise4();
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

	public orderitemPojo OrderItemInitialise3() throws ApiException{
		inventoryPojo i1= InvInitialise3();
		orderitemPojo oi=new orderitemPojo();
		oi.setName(i1.getName());
		oi.setBarcode(i1.getBarcode());
		oi.setQuantity(10);
		oi.setPrice(50);
		return oi;
	}

	public orderitemPojo OrderItemInitialise4() throws ApiException{
		inventoryPojo i1= InvInitialise4();
		orderitemPojo oi=new orderitemPojo();
		oi.setName(i1.getName());
		oi.setBarcode(i1.getBarcode());
		oi.setQuantity(5);
		oi.setPrice(300);
		return oi;
	}


	public void Initialise1()  throws ApiException {
		orderPojo order =new orderPojo();
		order.setT(ZonedDateTime.now().minus(Period.ofDays(1)));
		order.setInvoiceGenerated(false);
		order=dao.create(order);
		orderPojo order2 =new orderPojo();
		order2.setT(ZonedDateTime.now().minus(Period.ofDays(1)));
		order2.setInvoiceGenerated(false);
		order2=dao.create(order2);
		orderitemPojo oi1=OrderItemInitialise1();
		orderitemPojo oi2=OrderItemInitialise2();
		orderitemPojo oi3=OrderItemInitialise3();
		orderitemPojo oi4=OrderItemInitialise4();
		orderservice.AddSingleItem(oi1,order.getId());
		orderservice.AddSingleItem(oi2,order.getId());
		orderservice.AddSingleItem(oi3,order2.getId());
		orderservice.AddSingleItem(oi4,order2.getId());

	}

	@Test
	public void TestGetSalesWithoutBrandCategory() throws ApiException{
           Initialise1();
		   reportForm s=new reportForm();
	       s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
	       s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		   s.setBrand("none");
		   s.setCategory("none");
		   Map<Integer,List<Object>> m= service.getsales(s,service.getorder(s));
		   assertEquals(3, m.size());
		   List<Integer> brand_id = new ArrayList<Integer>(m.keySet());
           List<Object> o=m.get(brand_id.get(0));
		   assertEquals("nestle", o.get(2));
		   assertEquals("tea", o.get(3));
		   assertEquals(10, o.get(0));
		   assertEquals(1000.0, o.get(1));
		   o=m.get(brand_id.get(1));
		   assertEquals("bru", o.get(2));
		   assertEquals("tea", o.get(3));
		   assertEquals(15, o.get(0));
		   assertEquals(1500.0, o.get(1));
		   o=m.get(brand_id.get(2));
		   assertEquals("nestle", o.get(2));
		   assertEquals("coffee", o.get(3));
		   assertEquals(5, o.get(0));
		   assertEquals(1500.0, o.get(1));
	}

	@Test
	public void TestGetSalesWithoutCategory() throws ApiException{
           Initialise1();
		   reportForm s=new reportForm();
	       s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
	       s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		   s.setBrand("nestle");
		   s.setCategory("none");
		   Map<Integer,List<Object>> m= service.getsales(s,service.getorder(s));
		   assertEquals(2, m.size());
		   List<Integer> brand_id = new ArrayList<Integer>(m.keySet());
		   List<Object> o=m.get(brand_id.get(0));
		   assertEquals("nestle", o.get(2));
		   assertEquals("tea", o.get(3));
		   assertEquals(10, o.get(0));
		   assertEquals(1000.0, o.get(1));
		   o=m.get(brand_id.get(1));
		   assertEquals("nestle", o.get(2));
		   assertEquals("coffee", o.get(3));
		   assertEquals(5, o.get(0));
		   assertEquals(1500.0, o.get(1));
	}

	@Test
	public void TestGetSalesWithoutBrand() throws ApiException{
           Initialise1();
		   reportForm s=new reportForm();
	       s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
	       s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		   s.setBrand("none");
		   s.setCategory("tea");
		   Map<Integer,List<Object>> m= service.getsales(s,service.getorder(s));
		   assertEquals(2, m.size());
		   List<Integer> brand_id = new ArrayList<Integer>(m.keySet());
           List<Object> o=m.get(brand_id.get(0));
		   assertEquals("nestle", o.get(2));
		   assertEquals("tea", o.get(3));
		   assertEquals(10, o.get(0));
		   assertEquals(1000.0, o.get(1));
		   o=m.get(brand_id.get(1));
		   assertEquals("bru", o.get(2));
		   assertEquals("tea", o.get(3));
		   assertEquals(15, o.get(0));
		   assertEquals(1500.0, o.get(1));
	}

	@Test
	public void TestGetSales() throws ApiException{
           Initialise1();
		   reportForm s=new reportForm();
	       s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
	       s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		   s.setBrand("nestle");
		   s.setCategory("tea");
		   Map<Integer,List<Object>> m= service.getsales(s,service.getorder(s));
		   assertEquals(1, m.size());
		   List<Integer> brand_id = new ArrayList<Integer>(m.keySet());
           List<Object> o=m.get(brand_id.get(0));
		   assertEquals("nestle", o.get(2));
		   assertEquals("tea", o.get(3));
		   assertEquals(10, o.get(0));
		   assertEquals(1000.0, o.get(1));
	}

	@Test
	public void TestDaySalesReport() throws Exception{
		   Initialise1();
		   service.add();
		   reportForm s=new reportForm();
		   s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
		   s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		   System.err.print(s.getFrom());
		   System.err.print(s.getTo());
		   daySalesReportPojo r= service.get(s).get(0);
		   System.err.print(r.getDate());
           for(orderitemPojo oi:orderservice.get(1)){
			System.err.print(oi.getId());
		   }
		   assertEquals(2, r.getTotal_orders());
		   assertEquals(4, r.getTotal_items());
		   assertEquals(4000, r.getRevenue(),0.01);
		   assertEquals(ZonedDateTime.now().minus(Period.ofDays(1)).format(formatter), r.getDate().format(formatter));
	}

	@Test
	public void TestDaySalesReportNoData() throws Exception{
		   service.add();
		   reportForm s=new reportForm();
		   s.setFrom(ZonedDateTime.now().minus(Period.ofDays(2)));
		   s.setTo(ZonedDateTime.now().plus(Period.ofDays(1)));
		   daySalesReportPojo r= service.get(s).get(0);
		   assertEquals(0, r.getTotal_orders());
		   assertEquals(0, r.getTotal_items());
		   assertEquals(0, r.getRevenue(),0.01);
		   assertEquals(ZonedDateTime.now().minus(Period.ofDays(1)).format(formatter), r.getDate().format(formatter));
	}
 
	@Test
	public void TestCheckDateRangeNoFrom() throws Exception{
		reportForm s=new reportForm();
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Please enter valid from date");
		service.CheckDateRange(s);
	}

	@Test
	public void TestCheckDateRangeFromFreater() throws Exception{
		reportForm s=new reportForm();
		s.setFrom(ZonedDateTime.now());
		s.setTo(ZonedDateTime.now().minusDays(1));
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("From date is greater than to date");
		service.CheckDateRange(s);
	}



	@Test
	public void TestCheckDateRangeNoTo() throws Exception{
		reportForm s=new reportForm();
		s.setFrom(ZonedDateTime.now());
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Please enter valid to date");
		service.CheckDateRange(s);
	}


	@Test
	public void TestGetOrder() throws ApiException{
	Initialise1();
	reportForm s=new reportForm();
	s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
	s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
	Map<String,orderitemPojo>m=service.getorder(s);
	assertEquals(5, m.get("1").getId());
	assertEquals(6, m.get("12").getId());
	assertEquals(3, m.get("1").getOrder_id());
	assertEquals(3, m.get("12").getOrder_id());
	assertEquals("1", m.get("1").getBarcode());
	assertEquals("12", m.get("12").getBarcode());
	}

	@Test
	public void TestGetInventory() throws Exception{
	Initialise1();
	reportForm s=new reportForm();
	s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
	s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
	Map<String,inventoryPojo>m=service.getinventory();
	assertEquals("1", m.get("1").getBarcode());
	assertEquals("12", m.get("12").getBarcode());
	assertEquals(10, m.get("1").getQuantity());
	assertEquals(20, m.get("12").getQuantity());
	}

	@Test
	public void TestInventoryReport() throws Exception{
		InvInitialise();
		reportForm s=new reportForm();
	    s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
	    s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		Map<Integer,List<Object>> m=service.getinventoryReport(service.getinventory());
		List<Integer> brand_id = new ArrayList<Integer>(m.keySet());
		List<Object> o=m.get(brand_id.get(0));
		assertEquals("nestle", o.get(1));
		assertEquals("tea", o.get(2));
		assertEquals(10, o.get(0));
		o=m.get(brand_id.get(1));
		assertEquals("bru", o.get(1));
		assertEquals("tea", o.get(2));
		assertEquals(30, o.get(0));
		o=m.get(brand_id.get(2));
		assertEquals("nestle", o.get(1));
		assertEquals("coffee", o.get(2));
		assertEquals(20, o.get(0));
	}

	}

	