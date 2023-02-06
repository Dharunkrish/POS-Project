package com.increff.employee.service;

import static org.junit.Assert.assertEquals;
import org.junit.rules.ExpectedException;
import org.junit.Rule;
import java.util.List;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.productPojo;


public class ProductServiceTest extends AbstractUnitTest {

	@Autowired
	private productService service;

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
    
	public productPojo Initialise1()  throws ApiException {
		productPojo p = new productPojo();
        p.setName("150 g");
		p.setBarcode("1");
		p.setMrp(10.0);
		p.setBrand_Category_id(1);
		service.add(p);
		return p;
	}

	public productPojo Initialise2()  throws ApiException {
		productPojo p = new productPojo();
        p.setName("1 Liter");
		p.setBarcode("2");
		p.setMrp(20.0);
		p.setBrand_Category_id(2);
		service.add(p);
		return p;
	}

	@Test
	public void TestAdd() throws ApiException {
		productPojo p = new productPojo();
		p.setName("150 g");
		p.setBarcode("1");
		p.setMrp(10.0);
		p.setBrand_Category_id(1);
		service.add(p);
		assertEquals("150 g", p.getName());
		assertEquals("1", p.getBarcode());
		assertEquals(10.0, p.getMrp(),0.01);
		assertEquals(1, p.getBrand_Category_id());
	}

	
	@Test
	public void TestNormalize() {
		productPojo p = new productPojo();
		String name=" 150 G  ";
		String barcode=" 1 ";
		p.setName(name);
		p.setBarcode(barcode);
		productService.normalize(p);
		assertEquals("150 g", p.getName());
		assertEquals("1", p.getBarcode());
	}

	@Test
	public void TestCheckBarcode() throws ApiException{
		Initialise1();
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Barcode must be unique");
        service.bar("1",0);
	}

	@Test
	public void TestGetProductId() throws ApiException{
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Product with given ID does not exist, id: -1");
        service.getCheck(-1);
	}

	@Test
	public void TestGetProduct() throws Exception{
		Initialise1();
		int id=service.getAll().get(0).getProduct_id();
        productPojo p=service.get(id);
		assertEquals("150 g", p.getName());
		assertEquals("1", p.getBarcode());
		assertEquals(10.0, p.getMrp(),0.1);
		assertEquals(1, p.getBrand_Category_id());
	}

	@Test
	public void TestGetAllProduct() throws Exception{
		Initialise1();
		Initialise2();
        List<productPojo> p=service.getAll();
		assertEquals(2, p.size());
		productPojo p1=p.get(0);
		productPojo p2=p.get(1);
		assertEquals("150 g", p1.getName());
		assertEquals("1", p1.getBarcode());
		assertEquals(10.0, p1.getMrp(),0.1);
		assertEquals(1, p1.getBrand_Category_id());
		assertEquals("1 liter", p2.getName());
		assertEquals("2", p2.getBarcode());
		assertEquals(20.0, p2.getMrp(),0.1);
		assertEquals(2, p2.getBrand_Category_id());
	}

	@Test
	public void TestProductUpdate() throws Exception{
		Initialise1();
		int id=service.getAll().get(0).getProduct_id();
		productPojo p=service.get(id);
        p.setName("2 liter");
		p.setBarcode("120");
		p.setMrp(200.0);
		p.setBrand_Category_id(3);
		System.err.print(service.getAll().get(0).getBarcode());
        service.update(id, p);
		productPojo p2=service.get(id);
		assertEquals("2 liter", p2.getName());
		assertEquals("120", p2.getBarcode());
		assertEquals(200.0, p2.getMrp(),0.1);
		assertEquals(3, p2.getBrand_Category_id());
	}
}
