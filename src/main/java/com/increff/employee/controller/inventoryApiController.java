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

import com.increff.employee.model.Form.inventoryForm;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.util.DataConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api/inventory")
public class inventoryApiController {
	private Logger logger = Logger.getLogger(inventoryApiController.class);

	@Autowired
	private InventoryService service;

	@ApiOperation(value = "Adds an inventory")
	@RequestMapping(path = "/supervisor", method = RequestMethod.POST)
	public void add(@RequestBody inventoryForm form) throws ApiException {
		service.add(form);
	}

	
	

	@ApiOperation(value = "Gets an inventory by ID")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public inventoryForm get(@PathVariable int id) throws ApiException {
		return service.get(id);
	}
	
	@ApiOperation(value = "Gets list of products")
	@RequestMapping(path = "/id", method = RequestMethod.GET)
	public List<productPojo> getid() throws Exception {
		return service.getid();
	}

	@ApiOperation(value = "Gets list of all inventorys")
	@RequestMapping(method = RequestMethod.GET)
	public List<inventoryForm> getAll() throws Exception {
		return service.getAll();
	}

	@ApiOperation(value = "Updates an inventory")
	@RequestMapping(path = "/supervisor/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody inventoryForm f) throws ApiException {
		service.update(id, f);
	}
	
}
