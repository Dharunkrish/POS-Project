package com.increff.employee.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.booForm;
import com.increff.employee.model.inventoryForm;
import com.increff.employee.model.orderitemForm;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.orderitemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class orderitemApiController {
	private Logger logger = Logger.getLogger(orderitemApiController.class);

	@Autowired
	private orderitemService service;

	@ApiOperation(value = "Checks a orderItem")
	@RequestMapping(path = "/api/orderitem/check", method = RequestMethod.POST)
	public booForm add(@RequestBody orderitemForm form) throws ApiException {
        orderitemPojo o=convert(form);
        productPojo p=service.checkitems(o);
        if (p.getProduct_id()==-1) {
        	return convert(0,p);
        }
        else if (p.getProduct_id()==-2) {
        	return convert(2,p);
        }
        return convert(1,p);
	}

	
	

	@ApiOperation(value = "Gets an orderitem by ID")
	@RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.GET)
	public orderitemPojo get(@PathVariable int id) throws ApiException {
		orderitemPojo p = service.getid(id);
		logger.info(p.getId());
		logger.info(p.getPrice());
		return p;
	}
	
	@ApiOperation(value = "Updates an orderitem")
	@RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.PUT)
	public booForm update(@PathVariable int id, @RequestBody orderitemForm f) throws ApiException {
        orderitemPojo o=convert(f);
        productPojo p=service.checkprod(o);
        if (p==null) {
        	p=new productPojo();
        	return convert(0,p);
        }
        int q=service.check(o,p,f.getOld_q());
        logger.info(q);
        if (q==-1) {
        	p=new productPojo();
        	return convert(2,p);
        }
        o.setName(p.getName());
		service.update(id, o,q,p);
		return convert(1,p);
	}


	/*@ApiOperation(value = "Gets list of all inventorys")
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

	*/
	

	private static orderitemForm convert(orderitemPojo p) {
		orderitemForm d = new orderitemForm();
		d.setQuantity(p.getQuantity());
		d.setBarcode(p.getBarcode());
		d.setPrice(d.getPrice());
		d.setName(p.getName());
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
	
	private static booForm convert(int is_p,productPojo p) {
		booForm form=new booForm();
		form.setIs_p(is_p);
		form.setName(p.getName());
		form.setBarcode(p.getBarcode());
		return form;
	}
}
