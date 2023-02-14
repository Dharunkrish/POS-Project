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

import com.increff.employee.model.daySalesReportForm;
import com.increff.employee.model.reportData;
import com.increff.employee.model.reportForm;
import com.increff.employee.pojo.daySalesReportPojo;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.service.reportService;
import com.increff.employee.util.DataConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class reportApiController {
	private Logger logger = Logger.getLogger(reportApiController.class);

	@Autowired
	private reportService service;
		
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


	@ApiOperation(value = "Gets the Sales report")
	@RequestMapping(path = "/api/reports/daySalesReport", method = RequestMethod.POST)
	public List<reportData> get(@RequestBody reportForm r) throws Exception {
		List<reportData> p=new ArrayList<>();
		List<daySalesReportPojo> d= service.get(r);
		for (daySalesReportPojo s:d) {
			p.add(DataConversionUtil.convert(s));
		}
		return p;
	}
	
	@ApiOperation(value = "Gets the Sales report")
	@RequestMapping(path = "/api/reports/salesReport", method = RequestMethod.POST)
	public List<daySalesReportForm> getsales(@RequestBody reportForm r) throws Exception {
		logger.info(r.getFrom());
		 Map<String,orderitemPojo> o=service.getorder(r);
		 Map<Integer,List<Object>> m=service.getsales(r,o);
		 List<daySalesReportForm> report=new ArrayList<>();
		 for (int b:m.keySet()) {
			 report.add(DataConversionUtil.convert2(m.get(b)));
		 }
		 return report;
	}
	
	@ApiOperation(value = "Gets the Sales report")
	@RequestMapping(path = "/api/reports/inventoryReport", method = RequestMethod.GET)
	public List<daySalesReportForm> getinventory() throws Exception {
		 Map<String,inventoryPojo> i=service.getinventory();
		 Map<Integer,List<Object>> m=service.getinventoryReport(i);
		 List<daySalesReportForm> report=new ArrayList<>();
		 for (int b:m.keySet()) {
			 report.add(DataConversionUtil.convert1(m.get(b)));
		 }
		 return report;
	}
	
}
