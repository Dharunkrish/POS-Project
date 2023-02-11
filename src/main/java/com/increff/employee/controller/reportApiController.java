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
			p.add(convert(s));
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
			 report.add(convert(m.get(b)));
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
			 report.add(convert1(m.get(b)));
		 }
		 return report;
	}
	
	public reportData convert(daySalesReportPojo d) {
		reportData r=new reportData();
		r.setDate(formatter.format(d.getDate()));
		r.setTotal_order(d.getTotal_orders());
		r.setTotal_item(d.getTotal_items());
		r.setRevenue(d.getRevenue());
		return r;
	}
	
	public daySalesReportForm convert(List<Object> p) {
		daySalesReportForm r=new daySalesReportForm();
		r.setCount((int) p.get(0));
		r.setRevenue((double) p.get(1));
		r.setBrand((String) p.get(2));
		r.setCategory((String) p.get(3));
		return r;
	}
	
	public daySalesReportForm convert1(List<Object> p) {
		daySalesReportForm r=new daySalesReportForm();
		r.setCount((int) p.get(0));
		r.setBrand((String) p.get(1));
		r.setCategory((String) p.get(2));
		return r;
	}
}
