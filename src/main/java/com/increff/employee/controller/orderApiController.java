package com.increff.employee.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.Form.OrderItemData;
import com.increff.employee.model.Form.orderForm;
import com.increff.employee.model.Form.orderitemForm;
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.orderitemService;
import com.increff.employee.util.DataConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api/order")
public class orderApiController {
	private Logger logger = Logger.getLogger(orderApiController.class);

	@Autowired
	private orderitemService service;
   
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	@ApiOperation(value = "Creating a order")
	@RequestMapping(path = "/supervisor", method = RequestMethod.POST)
	public void add(@RequestBody List<orderitemForm> form) throws ApiException {
		service.AddItems(form);
	}
	
	@ApiOperation(value = "Inserting an orderitem inside an order")
	@RequestMapping(path = "/supervisor/{id}", method = RequestMethod.POST)
	public void AddToExistingOrder(@PathVariable int id,@RequestBody orderitemForm form) throws ApiException {
			service.AddSingleItem(form,id);
	}

	@ApiOperation(value = "Gets list of all orders")
	@RequestMapping( method = RequestMethod.GET)
	public List<orderForm> getAll() throws Exception {
		return service.getAll();
	}
	
	

	@ApiOperation(value = "Gets all order items of an order")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public List<orderitemPojo> get(@PathVariable int id) throws ApiException {
		return service.get(id);
	}
	

	

	
}
