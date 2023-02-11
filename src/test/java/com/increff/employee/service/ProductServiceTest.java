package com.increff.employee.service;

import static org.junit.Assert.assertEquals;
import org.junit.rules.ExpectedException;
import org.junit.Rule;
import java.util.List;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.brandPojo;
import com.increff.employee.pojo.productPojo;


public class ProductServiceTest extends AbstractUnitTest {

	@Autowired
	private productService service;

	@Autowired
	private brandService brandservice;

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
    
	public brandPojo BrandInitialise1()  throws ApiException {
		brandPojo p = new brandPojo();
        p.setBrand("nestle");
        p.setCategory("maggi");
		brandservice.add(p);
		return p;
	}

	public brandPojo BrandInitialise2()  throws ApiException {
		brandPojo p = new brandPojo();
        p.setBrand("bru1");
        p.setCategory("coffee");
		brandservice.add(p);
		return p;
	}

	//Initialise Product 1
	public productPojo Initialise1()  throws ApiException {
		brandPojo b=BrandInitialise1();
		productPojo p = new productPojo();
        p.setName("150 g");
		p.setBarcode("1");
		p.setMrp(10.0);
		p.setBrand_Category_id(b.getId());
		service.add(p);
		return p;
	}

	//Initialise Product 2
	public productPojo Initialise2()  throws ApiException {
		brandPojo b=BrandInitialise2();
		productPojo p = new productPojo();
        p.setName("1 Liter");
		p.setBarcode("2");
		p.setMrp(20.0);
		p.setBrand_Category_id(b.getId());
		service.add(p);
		return p;
	}

	//Test Add Product
	@Test
	public void TestAdd() throws ApiException {
		brandPojo b=BrandInitialise1();
		productPojo p = new productPojo();
		p.setName("150 g");
		p.setBarcode("1");
		p.setMrp(10.0);
		p.setBrand_Category_id(b.getId());
		service.add(p);
		assertEquals("150 g", p.getName());
		assertEquals("1", p.getBarcode());
		assertEquals(10.0, p.getMrp(),0.01);
		assertEquals(b.getId(), p.getBrand_Category_id());
	}

	@Test
	public void TestAddWrong1() throws ApiException {
		Initialise2();
		productPojo p = new productPojo();
		p.setName("150 g");
		p.setBarcode("2");
		p.setMrp(10.0);
		p.setBrand_Category_id(1);
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Barcode must be unique");
		service.add(p);
	}

	@Test
	public void TestAddWrong2() throws ApiException {
		productPojo p = new productPojo();
		p.setName("150 g");
		p.setBarcode("2");
		p.setMrp(10.0);
		p.setBrand_Category_id(0);
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Brand and Category with given ID does not exist, id: 0");
		service.add(p);
	}

	@Test
	public void TestAddWrong3() throws ApiException {
		productPojo p = new productPojo();
		p.setName("150 g");
		p.setBarcode("2");
		p.setMrp(-10.0);
		p.setBrand_Category_id(0);
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("MRP cannot be negative");
		service.add(p);
	}

	//Test if normalize method trims and converts String into lower case
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


	//Test if Exception is raised if barcode is not unique
	@Test
	public void TestCheckBarcode() throws ApiException{
		Initialise1();
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Barcode must be unique");
        service.bar("1",0);
	}

	//Test if Exception is raised if product with given ID does not exist
	@Test
	public void TestGetProductId() throws ApiException{
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Product with given ID does not exist, id: -1");
        service.getCheck(-1);
	}


	//Test to get a product with ID
	@Test
	public void TestGetProduct() throws Exception{
		productPojo p1=Initialise1();
		int id=service.getAll().get(0).getProduct_id();
        productPojo p=service.get(id);
		assertEquals(p1.getName(), p.getName());
		assertEquals(p1.getBarcode(), p.getBarcode());
		assertEquals(p1.getMrp(), p.getMrp(),0.1);
		assertEquals(p1.getBrand_Category_id(), p.getBrand_Category_id());
	}

	//Test to get all Products
	@Test
	public void TestGetAllProduct() throws Exception{
		productPojo pr1=Initialise1();
		productPojo pr2=Initialise2();
        List<productPojo> p=service.getAll();
		assertEquals(2, p.size());
		productPojo p1=p.get(0);
		productPojo p2=p.get(1);
		assertEquals(pr1.getName(), p1.getName());
		assertEquals(pr1.getBarcode(), p1.getBarcode());
		assertEquals(pr1.getMrp(), p1.getMrp(),0.1);
		assertEquals(pr1.getProduct_id(), p1.getProduct_id());
		assertEquals(pr1.getBrand_Category_id(), p1.getBrand_Category_id());
		assertEquals(pr2.getName(), p2.getName());
		assertEquals(pr2.getBarcode(), p2.getBarcode());
		assertEquals(pr2.getMrp(), p2.getMrp(),0.1);
		assertEquals(pr2.getBrand_Category_id(), p2.getBrand_Category_id());
		assertEquals(pr2.getProduct_id(), p2.getProduct_id());
	}


	@Test
	public void TestProductUpdate() throws Exception{
		productPojo pr=Initialise1();
		brandPojo b=BrandInitialise2();
		productPojo p=new productPojo();
        p.setName("2 liter");
		p.setBarcode("120");
		p.setMrp(200.0);
		p.setBrand_Category_id(b.getId());
        service.update(pr.getProduct_id(), p);
		productPojo p2=service.get(pr.getProduct_id());
		assertEquals("2 liter", p2.getName());
		assertEquals("120", p2.getBarcode());
		assertEquals(200.0, p2.getMrp(),0.1);
		assertEquals(b.getId(), p2.getBrand_Category_id());
	}

	@Test
	public void TestProductWrongUpdate1() throws Exception{
		Initialise1();
		Initialise2();
		int id=service.getAll().get(0).getProduct_id();
		productPojo p=new productPojo();
        p.setName("2 liter");
		p.setBarcode("2");
		p.setMrp(200.0);
		p.setBrand_Category_id(3);
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Barcode must be unique");
        service.update(id,p);
}

	//Test to check if exception is thrown if try to update with non-existing brand category
	@Test
	public void TestProductWrongUpdate2() throws Exception{
		Initialise1();
		int id=service.getAll().get(0).getProduct_id();
		productPojo p=new productPojo();
        p.setName("2 liter");
		p.setBarcode("2");
		p.setMrp(200.0);
		p.setBrand_Category_id(0);
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Brand and Category with given ID does not exist, id: 0");
        service.update(id,p);
	}

	@Test
	public void TestUpdateWrong3() throws Exception {
		Initialise1();
		int id=service.getAll().get(0).getProduct_id();
		productPojo p = new productPojo();
		p.setName("150 g");
		p.setBarcode("2");
		p.setMrp(-10.0);
		p.setBrand_Category_id(0);
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("MRP cannot be negative");
		service.update(id,p);
	}

}
