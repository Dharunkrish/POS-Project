package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.productData;
import com.increff.employee.model.productForm;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.brandService;
import com.increff.employee.service.productService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class productApiController {

	@Autowired
	private productService service;
    private brandService bin;
	
	private Logger logger = Logger.getLogger(productApiController.class);

	@ApiOperation(value = "Adds an product")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody productForm form) throws ApiException {
		productPojo p = convert(form);
		logger.info(p.getBrand_Category());
		service.add(p);
	}

	
	@ApiOperation(value = "Deletes and brand")
	@RequestMapping(path = "/api/product/{product_id}", method = RequestMethod.DELETE)
	// /api/1
	public void delete(@PathVariable int product_id) {
		logger.info(product_id);
		service.delete(product_id);
	}

	@ApiOperation(value = "Gets an brand by ID")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
	public productData get(@PathVariable int id) throws ApiException {
		productPojo p = service.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of all brands")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<productData> getAll() throws Exception {
		 List<productPojo> list=service.getAll();
		List<productData> list2 = new ArrayList<productData>();
		for (productPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an brand")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody productForm f) throws ApiException {
		productPojo p = convert(f);
		service.update(id, p);
	}
	

	private static productData convert(productPojo p) {
		productData d = new productData();
		d.setBrand_Category(p.getBrand_Category());
		d.setName(p.getName());
		d.setBarcode(p.getBarcode());
		d.setMrp(p.getMrp());
		d.setProduct_id(p.getProduct_id());
		return d;
	}

	private productPojo convert(productForm f) throws ApiException {
		productPojo p=new productPojo();
		p.setName(f.getName());
		p.setBarcode(f.getBarcode());
		p.setMrp(f.getMrp());
		p.setBrand_Category(f.getBrand_Category());
		return p;
	}
	
}
