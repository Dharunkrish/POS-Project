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

import com.increff.employee.model.brandData;
import com.increff.employee.model.brandForm;
import com.increff.employee.pojo.brandPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.brandService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandApiController {

	@Autowired
	private brandService service;

	private Logger logger = Logger.getLogger(BrandApiController.class);

	@ApiOperation(value = "Adds an product")
	@RequestMapping(path = "/api/brand/supervisor", method = RequestMethod.POST)
	public void add(@RequestBody brandForm form) throws ApiException {
		brandPojo p = convert(form);
		service.add(p);
	}
	
	@ApiOperation(value = "Adds an product")
	@RequestMapping(path = "/api/brand", method = RequestMethod.POST)
	public List<brandData> get(@RequestBody brandForm form) throws ApiException {
		brandPojo p = convert(form);
		List<brandPojo> list =service.getbrand(p);
		List<brandData> list2 = new ArrayList<brandData>();
		for (brandPojo b : list) {
			list2.add(convert(b));
		}
		return list2;
	}


	@ApiOperation(value = "Gets an brand by ID")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
	public brandData get(@PathVariable int id) throws ApiException {
		brandPojo p = service.get(id);
		return convert(p);
	}
	

	
	@ApiOperation(value = "Gets list of all brands")
	@RequestMapping(path = "/api/brand", method = RequestMethod.GET)
	public List<brandData> getAll() {
		List<brandPojo> list = service.getAll();
		List<brandData> list2 = new ArrayList<brandData>();
		for (brandPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an brand")
	@RequestMapping(path = "/api/brand/supervisor/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody brandForm f) throws ApiException {
		brandPojo p = convert(f);
		service.update(id, p);
	}
	

	private static brandData convert(brandPojo p) {
		brandData d = new brandData();
		d.setBrand(p.getBrand());
		d.setCategory(p.getCategory());
		d.setId(p.getId());
		return d;
	}

	private static brandPojo convert(brandForm f) {
		brandPojo p = new brandPojo();
		p.setBrand(f.getBrand());
		p.setCategory(f.getCategory());
		return p;
	}
}
