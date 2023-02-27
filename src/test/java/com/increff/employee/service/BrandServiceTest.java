package com.increff.employee.service;

import static org.junit.Assert.assertEquals;
import org.junit.rules.ExpectedException;
import org.junit.Rule;
import java.util.List;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.model.Data.brandData;
import com.increff.employee.model.Form.brandForm;
import com.increff.employee.pojo.brandPojo;


public class BrandServiceTest extends AbstractUnitTest {

	@Autowired
	private brandService service;

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
    
	public brandForm Initialise1()  throws ApiException {
		brandForm p = new brandForm();
        p.setBrand("nestle");
        p.setCategory("maggi");
		service.add(p);
		return p;
	}

	public brandForm Initialise2()  throws ApiException {
		brandForm p = new brandForm();
        p.setBrand("bru1");
        p.setCategory("coffee");
		service.add(p);
		return p;
	}

	public brandForm Initialise3()  throws ApiException {
		brandForm p = new brandForm();
        p.setBrand("bru1");
        p.setCategory("maggi");
		service.add(p);
		return p;
	}

	public brandForm Initialise4()  throws ApiException {
		brandForm p = new brandForm();
        p.setBrand("nestle");
        p.setCategory("coffee");
		service.add(p);
		return p;
	}

	@Test
	public void TestAdd() throws ApiException {
		brandForm p = new brandForm();
        p.setBrand("nestle");
        p.setCategory("maggi");
		service.add(p);
		assertEquals("nestle", p.getBrand());
		assertEquals("maggi", p.getCategory());
	}

	
	@Test
	public void TestNormalize() {
		brandPojo p = new brandPojo();
		String brand=" Nestle  ";
		String category=" MAGGi ";
		p.setBrand(brand);
		p.setCategory(category);
		brandService.normalize(p);
		assertEquals("nestle", p.getBrand());
		assertEquals("maggi", p.getCategory());
	}

	@Test
	public void TestCheckBrand() throws ApiException{
		Initialise1();
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Brand nestle and Category maggi combination already exist");
        service.insertCheck("nestle", "maggi");
	}

	@Test
	public void TestGetBrandId() throws ApiException{
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Brand and Category with given ID does not exist, id: -1");
        service.getCheck(-1);
	}

	@Test
	public void TestGetBrandCategory() throws ApiException{
		Initialise1();
		int id=service.getAll().get(0).getId();
        brandForm p=service.get(id);
		assertEquals("nestle", p.getBrand());
		assertEquals("maggi", p.getCategory());
	}

	@Test
	public void TestGetAllBrandCategory() throws ApiException{
		Initialise1();
		Initialise2();
        List<brandData> b=service.getAll();
		assertEquals(2, b.size());
		brandData p1=b.get(0);
		brandData p2=b.get(1);
		assertEquals("nestle", p1.getBrand());
		assertEquals("maggi", p1.getCategory());
		assertEquals("bru1", p2.getBrand());
		assertEquals("coffee", p2.getCategory());
		}

		@Test
	public void TestUpdateBrandCategory() throws ApiException{
		Initialise1();
		int id=service.getAll().get(0).getId();
		brandForm b=new brandForm();
        b.setBrand("tupperware");
		b.setCategory("bottle");
        service.update(id,b);
		brandForm b1=service.get(id);
		assertEquals("tupperware", b1.getBrand());
		assertEquals("bottle", b1.getCategory());
	}

	@Test
	public void TestUpdateWrongBrandCategory() throws ApiException{
		Initialise1();
		brandData b=service.getAll().get(0);
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Brand nestle and Category maggi combination already exist");
		service.update(b.getId(),b);
	}

	@Test
	public void TestGetBrandWithoutBrandCategory() throws ApiException{
		Initialise1();
		Initialise2();
		brandForm b=new brandForm();
		List<brandData> brandItems=service.getbrand(b);
		brandForm p1=brandItems.get(0);
		brandForm p2=brandItems.get(1);
		assertEquals("nestle", p1.getBrand());
		assertEquals("maggi", p1.getCategory());
		assertEquals("bru1", p2.getBrand());
		assertEquals("coffee", p2.getCategory());
	}


	@Test
	public void TestGetBrandWithoutCategory() throws ApiException{
		Initialise1();
		Initialise4();
		brandForm b=new brandForm();
		b.setBrand("nestle");
		List<brandData> brandItems=service.getbrand(b);
		brandForm p1=brandItems.get(0);
		brandForm p2=brandItems.get(1);
		assertEquals("nestle", p1.getBrand());
		assertEquals("maggi", p1.getCategory());
		assertEquals("nestle", p2.getBrand());
		assertEquals("coffee", p2.getCategory());
	}

	@Test
	public void TestGetBrandWithoutBrand() throws ApiException{
		Initialise1();
		Initialise2();
		Initialise3();
		brandForm b=new brandForm();
		b.setCategory("maggi");
		List<brandData> brandItems=service.getbrand(b);
		brandForm p1=brandItems.get(0);
		brandForm p2=brandItems.get(1);
		assertEquals("nestle", p1.getBrand());
		assertEquals("maggi", p1.getCategory());
		assertEquals("bru1", p2.getBrand());
		assertEquals("maggi", p2.getCategory());
	}
}
