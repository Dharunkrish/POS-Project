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
import com.increff.employee.model.Data.daysalesData;
import com.increff.employee.model.Form.reportForm;
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

		public brandPojo BrandInitialise5() throws ApiException {
			brandPojo p1 = new brandPojo();
			p1.setBrand("nestle");
			p1.setCategory("maggi");
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
		oi.setPrice(90);
		return oi;
	}

	public orderitemPojo OrderItemInitialise2() throws ApiException{
		inventoryPojo i1= InvInitialise2();
		orderitemPojo oi=new orderitemPojo();
		oi.setName(i1.getName());
		oi.setBarcode(i1.getBarcode());
		oi.setQuantity(5);
		oi.setPrice(80);
		return oi;
	}

	public orderitemPojo OrderItemInitialise3() throws ApiException{
		inventoryPojo i1= InvInitialise3();
		orderitemPojo oi=new orderitemPojo();
		oi.setName(i1.getName());
		oi.setBarcode(i1.getBarcode());
		oi.setQuantity(10);
		oi.setPrice(90);
		return oi;
	}

	public orderitemPojo OrderItemInitialise4() throws ApiException{
		inventoryPojo i1= InvInitialise4();
		orderitemPojo oi=new orderitemPojo();
		oi.setName(i1.getName());
		oi.setBarcode(i1.getBarcode());
		oi.setQuantity(5);
		oi.setPrice(90);
		return oi;
	}


	public void Initialise1()  throws Exception {
		orderitemPojo oi1=OrderItemInitialise1();
		orderitemPojo oi2=OrderItemInitialise2();
		orderitemPojo oi3=OrderItemInitialise3();
		orderitemPojo oi4=OrderItemInitialise4();
		List<orderitemPojo> o=new ArrayList<orderitemPojo> ();
		o.add(oi1);
		o.add(oi2);
		orderservice.AddItems(o);
		orderPojo o1=orderservice.getAll().get(0);
		o1.setId(o1.getId());
		o1.setT(ZonedDateTime.now().minus(Period.ofDays(1)));
		orderservice.update(o1.getId(), o1);
		List<orderitemPojo> or=new ArrayList<orderitemPojo> ();
		or.add(oi3);
		or.add(oi4);
		orderservice.AddItems(or);
		orderPojo o2=orderservice.getAll().get(1);
		o2.setId(o2.getId());
		o2.setT(ZonedDateTime.now().minus(Period.ofDays(1)));
		orderservice.update(o2.getId(), o2);
	}


	@Test
	public void TestGetSalesWithoutBrandCategory() throws Exception{
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
		   assertEquals(900.0, o.get(1));
		   o=m.get(brand_id.get(1));
		   assertEquals("bru", o.get(2));
		   assertEquals("tea", o.get(3));
		   assertEquals(15, o.get(0));
		   assertEquals(1300.0, o.get(1));
		   o=m.get(brand_id.get(2));
		   assertEquals("nestle", o.get(2));
		   assertEquals("coffee", o.get(3));
		   assertEquals(5, o.get(0));
		   assertEquals(450.0, o.get(1));
	}

	@Test
	public void TestGetSalesWithoutCategory() throws Exception{
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
		   assertEquals(900.0, o.get(1));
		   o=m.get(brand_id.get(1));
		   assertEquals("nestle", o.get(2));
		   assertEquals("coffee", o.get(3));
		   assertEquals(5, o.get(0));
		   assertEquals(450.0, o.get(1));
	}

	@Test
	public void TestGetSalesWithoutBrand() throws Exception{
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
		   assertEquals(900.0, o.get(1));
		   o=m.get(brand_id.get(1));
		   assertEquals("bru", o.get(2));
		   assertEquals("tea", o.get(3));
		   assertEquals(15, o.get(0));
		   assertEquals(1300.0, o.get(1));
	}

	@Test
	public void TestGetSales() throws Exception{
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
		   assertEquals(900.0, o.get(1));
	}

	@Test
	public void TestGetSalesWrong() throws Exception{
	Initialise1();
	BrandInitialise5();
	reportForm s=new reportForm();
	s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
	s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
	s.setBrand("bru");
	s.setCategory("maggi");
	exceptionRule.expect(ApiException.class);
	exceptionRule.expectMessage("Brand "+s.getBrand()+" and Category "+s.getCategory()+" combination does not exist");
	Map<Integer,List<Object>> m= service.getsales(s,service.getorder(s));
	}

	@Test
	public void TestDaySalesReport() throws Exception{
		   Initialise1();
		   service.add();
		   reportForm s=new reportForm();
		   s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
		   s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		   daySalesReportPojo r= service.get(s).get(0);
		   assertEquals(2, r.getTotal_orders());
		   assertEquals(4, r.getTotal_items());
		   assertEquals(2650, r.getRevenue(),0.01);
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
	public void TestGetOrder() throws Exception{
	Initialise1();
	reportForm s=new reportForm();
	s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
	s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
	Map<String,orderitemPojo>m=service.getorder(s);
	assertEquals(orderservice.getAll().get(0).getId(), m.get("1").getOrder_id());
	assertEquals(orderservice.getAll().get(0).getId(), m.get("12").getOrder_id());
	assertEquals(orderservice.getAll().get(1).getId(), m.get("123").getOrder_id());
	assertEquals(orderservice.getAll().get(1).getId(), m.get("1234").getOrder_id());
	assertEquals("1", m.get("1").getBarcode());
	assertEquals("12", m.get("12").getBarcode());
	assertEquals("123", m.get("123").getBarcode());
	assertEquals("1234", m.get("1234").getBarcode());
	}

	@Test
	public void TestGetInventory() throws Exception{
	Initialise1();
	Map<String,inventoryPojo>m=service.getinventory();
	System.err.print(m);
	assertEquals("12", m.get("12").getBarcode());
	assertEquals("1234", m.get("1234").getBarcode());
	assertEquals(15, m.get("12").getQuantity());
	assertEquals(15, m.get("1234").getQuantity());
	}

	@Test
	public void TestInventoryReport() throws Exception{
		InvInitialise();
		reportForm s=new reportForm();
	    s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
	    s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		Map<Integer,List<Object>> m=service.getinventoryReport(service.getinventory());
		List<Integer> brand_id = new ArrayList<Integer>(m.keySet());
		List<Object> o=m.get(brand_id.get(2));
		System.err.print(m);
		assertEquals("nestle", o.get(1));
		assertEquals("coffee", o.get(2));
		assertEquals(20, o.get(0));
		o=m.get(brand_id.get(0));
		System.err.print(m);
		assertEquals("nestle", o.get(1));
		assertEquals("tea", o.get(2));
		assertEquals(10, o.get(0));
		o=m.get(brand_id.get(1));
		assertEquals("bru", o.get(1));
		assertEquals("tea", o.get(2));
		assertEquals(30, o.get(0));
	}

	@Test
	public void TestInventoryReportAfterDelete() throws Exception{
		Initialise1();
		reportForm s=new reportForm();
	    s.setFrom(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()).minus(Period.ofDays(2)));
	    s.setTo(LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault()));
		Map<Integer,List<Object>> m=service.getinventoryReport(service.getinventory());
		List<Integer> brand_id = new ArrayList<Integer>(m.keySet());
		System.err.print("kk");
		System.err.print(m);
		List<Object> o=m.get(brand_id.get(0));
		assertEquals("bru", o.get(1));
		assertEquals("tea", o.get(2));
		assertEquals(15, o.get(0));
		o=m.get(brand_id.get(1));
		assertEquals("nestle", o.get(1));
		assertEquals("coffee", o.get(2));
		assertEquals(15, o.get(0));
	}
	}

	