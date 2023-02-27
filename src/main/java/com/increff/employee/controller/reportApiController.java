package com.increff.employee.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.Data.reportData;
import com.increff.employee.model.Form.daySalesReportForm;
import com.increff.employee.model.Form.reportForm;
import com.increff.employee.pojo.daySalesReportPojo;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.service.reportService;
import com.increff.employee.util.DataConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api/reports/")
public class reportApiController {
	private Logger logger = Logger.getLogger(reportApiController.class);

	@Autowired
	private reportService service;
		
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


	@ApiOperation(value = "Gets the Sales report")
	@RequestMapping(path = "daySalesReport", method = RequestMethod.POST)
	public List<reportData> get(@RequestBody reportForm r) throws Exception {
		return service.get(r);
	}
	
	@ApiOperation(value = "Gets the Sales report")
	@RequestMapping(path = "salesReport", method = RequestMethod.POST)
	public List<daySalesReportForm> getsales(@RequestBody reportForm r) throws Exception {
		 return service.getsales(r);
	}
	
	@ApiOperation(value = "Gets the Sales report")
	@RequestMapping(path = "inventoryReport", method = RequestMethod.GET)
	public List<daySalesReportForm> getinventory() throws Exception {
		 return service.getinventoryReport();
	}
	
}
