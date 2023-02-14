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

import com.increff.employee.model.inventoryForm;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.util.DataConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class inventoryApiController {
	private Logger logger = Logger.getLogger(inventoryApiController.class);

	@Autowired
	private InventoryService service;

	@ApiOperation(value = "Adds an product")
	@RequestMapping(path = "/api/inventory/supervisor", method = RequestMethod.POST)
	public void add(@RequestBody inventoryForm form) throws ApiException {
		inventoryPojo p = DataConversionUtil.convert(form);
		service.add(p);
	}

	
	

	@ApiOperation(value = "Gets an inventory by ID")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
	public inventoryForm get(@PathVariable int id) throws ApiException {
		inventoryPojo p = service.get(id);
		return DataConversionUtil.convert(p);
	}
	
	@ApiOperation(value = "Gets an inventory by ID")
	@RequestMapping(path = "/api/inventory/id", method = RequestMethod.GET)
	public List<productPojo> getid() throws Exception {
		List<productPojo> list = service.getid();
		return list;
	}

	@ApiOperation(value = "Gets list of all inventorys")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<inventoryForm> getAll() throws Exception {
		List<inventoryPojo> list = service.getAll();
		List<inventoryForm> list2 = new ArrayList<inventoryForm>();
		for (inventoryPojo p : list) {
			list2.add(DataConversionUtil.convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an inventory")
	@RequestMapping(path = "/api/inventory/supervisor/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody inventoryForm f) throws ApiException {
		inventoryPojo p = DataConversionUtil.convert(f);
		service.update(id, p);
	}
	
}
