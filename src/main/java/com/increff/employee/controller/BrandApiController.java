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

import com.increff.employee.model.Data.brandData;
import com.increff.employee.model.Form.brandForm;
import com.increff.employee.pojo.brandPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.brandService;
import com.increff.employee.util.DataConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api/brand")
public class BrandApiController {

	@Autowired
	private brandService service;

	private Logger logger = Logger.getLogger(BrandApiController.class);

	@ApiOperation(value = "Adds an product")
	@RequestMapping(path = "/supervisor", method = RequestMethod.POST)
	public void add(@RequestBody brandForm form) throws ApiException {
		service.add(form);
	}
	
	@ApiOperation(value = "Gets brandList")
	@RequestMapping(method = RequestMethod.POST)
	public List<brandData> get(@RequestBody brandForm form) throws ApiException {
		return service.getbrand(form);
		
	}


	@ApiOperation(value = "Gets an brand by ID")
	@RequestMapping(path="/{id}", method = RequestMethod.GET)
	public brandData get(@PathVariable int id) throws ApiException {
		return service.get(id);
	}
	

	
	@ApiOperation(value = "Gets list of all brands")
	@RequestMapping(method = RequestMethod.GET)
	public List<brandData> getAll() {
		return service.getAll();
	}

	@ApiOperation(value = "Updates an brand")
	@RequestMapping(path = "/supervisor/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody brandForm f) throws ApiException {
		service.update(id, f);
	}
}
