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
   
	@ApiOperation(value = "Creating a order")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public booForm add(@RequestBody List<orderitemForm> form) throws ApiException {
		List <orderitemPojo> item=new ArrayList<orderitemPojo>();
		for(orderitemForm f:form) {
			orderitemPojo o=convert(f);
			productPojo p=service.checkprod(o);
			int q=service.check(o, p);
			if (q==-1) {
				return convert(2,o.getBarcode());
			}	
			item.add(o);
		}
		int id=service.create();
		logger.info(id);
		for(orderitemPojo order:item) {
			order.setOrder_id(id);
			service.add(order);
		}
		return convert(1,"");
		
	}

	@ApiOperation(value = "Gets list of all orders")
	@RequestMapping(path = "/api/order", method = RequestMethod.GET)
	public List<orderForm> getAll() throws Exception {
		List<orderPojo> list = service.getAll();
		List<orderForm> list2 = new ArrayList<orderForm>();
		for (orderPojo p : list) {
			logger.info(p.getId());
			list2.add(convert(p));
		}
		return list2;
	}
	
	

	@ApiOperation(value = "Gets all order items of an order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
	public List<orderitemPojo> get(@PathVariable int id) throws ApiException {
		return service.get(id);
		
	}
	
	/*@ApiOperation(value = "Gets an inventory by ID")
	@RequestMapping(path = "/api/inventory/id", method = RequestMethod.GET)
	public List<productDTO> getid() throws Exception {
		List<productDTO> list = service.getid();
		List<inventoryForm> list2 = new ArrayList<inventoryForm>();
		return list;
	}

	@ApiOperation(value = "Gets list of all inventorys")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<inventoryForm> getAll() throws Exception {
		List<inventoryPojo> list = service.getAll();
		List<inventoryForm> list2 = new ArrayList<inventoryForm>();
		for (inventoryPojo p : list) {
			logger.info(p.getId());
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an inventory")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody inventoryForm f) throws ApiException {
		inventoryPojo p = convert(f);
		logger.info(p.getQuantity());
		logger.info(id);
		service.update(id, p);
	}*/
	

	private static orderitemForm convert(orderitemPojo p) {
		orderitemForm d = new orderitemForm();
		d.setQuantity(p.getQuantity());
		d.setBarcode(p.getBarcode());
		d.setPrice(d.getPrice());
		return d;
	}
	
	private static orderitemForm convert(int is_p,productPojo p) {
		orderitemForm form=new orderitemForm();
		form.setName(p.getName());
		form.setBarcode(p.getBarcode());
		return form;
	}

	private static orderForm convert(orderPojo p) {
		orderForm d = new orderForm();
		d.setId(p.getId());
		d.setTime(p.getT().toString());
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
	
	
	
	private static booForm convert(int is_p,String barcode) {
		booForm form=new booForm();
		form.setIs_p(is_p);
		form.setBarcode(barcode);
		return form;
	}
	

	
}
