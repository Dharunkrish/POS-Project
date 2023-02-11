package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.dao.inventoryDao;
import com.increff.employee.dao.orderitemDao;
import com.increff.employee.pojo.brandPojo;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.productPojo;



public class InventoryServiceTest extends AbstractUnitTest {

	@Autowired
	private InventoryService service;

	@Autowired
	private productService prodservice;

	@Autowired
	private brandService brandservice;

	@Autowired
	private inventoryDao indao;

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	private Logger logger = Logger.getLogger(orderitemDao.class);

    
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
	public productPojo prodInitialise1()  throws ApiException {
		brandPojo b=BrandInitialise1();
		productPojo p = new productPojo();
        p.setName("150 g");
		p.setBarcode("1");
		p.setMrp(10.0);
		p.setBrand_Category_id(b.getId());
		prodservice.add(p);
		return p;
	}

	//Initialise Product 2
	public productPojo prodInitialise2()  throws ApiException {
		brandPojo b=BrandInitialise2();
		productPojo p = new productPojo();
        p.setName("1 Liter");
		p.setBarcode("2");
		p.setMrp(20.0);
		p.setBrand_Category_id(b.getId());
		prodservice.add(p);
		return p;
	}

	public inventoryPojo Initialise1()  throws ApiException {
		productPojo p=prodInitialise1();
		inventoryPojo i = new inventoryPojo();
		i.setId(p.getProduct_id());
        i.setName("150 g");
		i.setBarcode("1");
		i.setQuantity(10);
		service.add(i);
		return i;
	}

	public inventoryPojo Initialise2()  throws ApiException {
	    productPojo p=prodInitialise2();
		inventoryPojo i = new inventoryPojo();
        i.setId(p.getProduct_id());
        i.setName("1 Liter");
		i.setBarcode("2");
		i.setQuantity(20);
		service.add(i);
		return i;
	}

	public inventoryPojo Initialise3()  throws ApiException {
		inventoryPojo p = new inventoryPojo();
        p.setId(2);
        p.setName("250 g");
		p.setBarcode("2");
		p.setQuantity(-30);
		return p;
	}

	@Test
	public void TestAdd() throws Exception {
		productPojo i=prodInitialise1();
		inventoryPojo p = new inventoryPojo();
		System.out.print(prodservice.getAll().size());
		p.setId(i.getProduct_id());
        p.setName("150 g");
		p.setBarcode("1");
		p.setQuantity(10);
		service.add(p);
		assertEquals(i.getProduct_id(), p.getId());
		assertEquals("150 g", p.getName());
		assertEquals("1", p.getBarcode());
		assertEquals(10, p.getQuantity());
	}

	@Test
	public void TestAddWrong1() throws Exception {
		productPojo i=prodInitialise1();
		inventoryPojo p = new inventoryPojo();
		System.out.print(prodservice.getAll().size());
		p.setId(i.getProduct_id());
        p.setName("150 g");
		p.setBarcode("1");
		p.setQuantity(-10);
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Quantity should not be zero or negative");
		service.add(p);
	}

	@Test
	public void TestCheckQuantity() throws ApiException{
		inventoryPojo i =Initialise3();
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Quantity should not be zero or negative");
        service.QuantityCheck(i);
	}

	@Test
	public void TestProductCheck() throws ApiException{
		inventoryPojo i =Initialise3();
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Product with given Product ID does not exit, id: "+i.getId());
        service.ProductCheck(i.getId());
	}

	@Test
	public void TestInventoryCheck() throws ApiException{
		inventoryPojo i =Initialise1();
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Inventory details for the given Product ID already exists");
        service.InvCheck(i.getId());
	}

	@Test
	public void TestGetCheck() throws ApiException{
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("inventory with given ID does not exit, id: -1");
        service.getCheck(-1);
	}


	@Test
	public void TestGetInventory() throws Exception{
		inventoryPojo i=Initialise1();
        inventoryPojo p=service.get(i.getId());
		assertEquals(i.getId(), p.getId());
		assertEquals("150 g", p.getName());
		assertEquals("1", p.getBarcode());
		assertEquals(10, p.getQuantity());
	}

	@Test
	public void TestGetAllInventory() throws Exception{
		inventoryPojo i1=Initialise1();
		inventoryPojo i2=Initialise2();
        List<inventoryPojo> p=service.getAll();
		assertEquals(2, p.size());
		inventoryPojo p1=p.get(0);
		inventoryPojo p2=p.get(1);
		assertEquals(i1.getId(), p1.getId());
		assertEquals("150 g", p1.getName());
		assertEquals("1", p1.getBarcode());
		assertEquals(10, p1.getQuantity());
		assertEquals(i2.getId(), p2.getId());
		assertEquals("1 liter", p2.getName());
		assertEquals("2", p2.getBarcode());
		assertEquals(20, p2.getQuantity());
	}

	@Test
	public void TestInventoryUpdate() throws Exception{
		inventoryPojo p1= Initialise1();
		inventoryPojo p=new inventoryPojo();
		p.setQuantity(200);
        service.update(p1.getId(), p);
		inventoryPojo p2=service.get(service.getAll().get(0).getId());
		assertEquals(p1.getId(), p2.getId());
		assertEquals(200, p2.getQuantity());
	}

	@Test
	public void TestGetAllProduct() throws Exception{
		Initialise1();
		Initialise2();
        List<productPojo> p=service.getid();
		assertEquals(0, p.size());
	}

}
