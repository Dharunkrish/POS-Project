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

import com.increff.employee.model.booForm;
import com.increff.employee.model.orderForm;
import com.increff.employee.model.orderitemForm;
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.orderitemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class orderApiController {
	private Logger logger = Logger.getLogger(orderApiController.class);

	@Autowired
	private orderitemService service;
   
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	@ApiOperation(value = "Creating a order")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public booForm add(@RequestBody List<orderitemForm> form) throws ApiException {
		List <orderitemPojo> item=new ArrayList<orderitemPojo>();
		for(orderitemForm f:form) {
			item.add(convert(f));
		}
		return convert(service.AddItems(item));
	}
	
	@ApiOperation(value = "Creating a order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.POST)
	public booForm AddToExistingOrder(@PathVariable int id,@RequestBody orderitemForm form) throws ApiException {
			int q=service.AddSingleItem(convert(form),id);
		    return convert(q);
	}

	@ApiOperation(value = "Gets list of all orders")
	@RequestMapping(path = "/api/order", method = RequestMethod.GET)
	public List<orderForm> getAll() throws Exception {
		List<orderForm> list2 = new ArrayList<orderForm>();
		for (orderPojo p : service.getAll()) {
			list2.add(convert(p));
		}
		return list2;
	}
	
	

	@ApiOperation(value = "Gets all order items of an order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
	public List<orderitemPojo> get(@PathVariable int id) throws ApiException {
		return service.get(id);
	}
	
	private static orderForm convert(orderPojo p) {
		orderForm d = new orderForm();
		d.setId(p.getId());
		d.setTime(p.getT().format(formatter));
		d.setInvoiceGenerated(p.isInvoiceGenerated());
		return d;
	}

	private static orderitemPojo convert(orderitemForm f) {
		orderitemPojo o = new orderitemPojo();
		o.setQuantity(f.getQuantity());
		o.setBarcode(f.getBarcode());
		o.setPrice(f.getPrice());
		o.setName(f.getName());
		return o;
	}
	
	
	
	private static booForm convert(int is_p) {
		booForm form=new booForm();
		form.setIs_p(is_p);
		return form;
	}
	

	
}
