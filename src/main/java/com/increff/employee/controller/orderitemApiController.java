package com.increff.employee.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.Form.OrderItemData;
import com.increff.employee.model.Form.orderitemForm;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.orderitemService;
import com.increff.employee.util.DataConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api/orderitem")
public class orderitemApiController {
	private Logger logger = Logger.getLogger(orderitemApiController.class);

	@Autowired
	private orderitemService service;

	@ApiOperation(value = "Checks a orderItem")
	@RequestMapping(path = "/supervisor/check", method = RequestMethod.POST)
	public OrderItemData Add(@RequestBody orderitemForm form) throws ApiException {
        orderitemPojo o=DataConversionUtil.convert(form);
        return service.checkitem(form);
	}

	
	

	@ApiOperation(value = "Gets an orderitem by ID")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public orderitemPojo Get(@PathVariable int id) throws ApiException {
		return service.getid(id);
	}
	
	@ApiOperation(value = "Updates an orderitem")
	@RequestMapping(path = "/supervisor/{id}", method = RequestMethod.PUT)
	public OrderItemData Update(@PathVariable int id, @RequestBody orderitemForm f) throws ApiException {
		return DataConversionUtil.convert(service.update(id, f,f.getOld_q()),f.getName(),0);
	}
	
	@ApiOperation(value = "Gets an orderitem by ID")
	@RequestMapping(path = "/supervisor/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) throws ApiException {
		service.delete(id);
	}

}
