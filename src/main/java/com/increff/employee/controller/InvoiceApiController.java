package com.increff.employee.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.Form.reportForm;
import com.increff.employee.service.invoiceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api")
public class InvoiceApiController {
	@Autowired
	private invoiceService invoiceService;
   
	private Logger logger = Logger.getLogger(InvoiceApiController.class);

	@ApiOperation(value = "Generates an invoice")
	@RequestMapping(path = "/invoice/{id}", method = RequestMethod.GET)
	public void add(@PathVariable int id, HttpServletResponse response) throws Exception {
		invoiceService.generatePdf(id,response);
	}
	
	@ApiOperation(value = "Generates Daily Sales Report")
	@RequestMapping(path = "/report/pdf", method = RequestMethod.POST)
	public void addReport(@RequestBody reportForm r, HttpServletResponse response) throws Exception {
		invoiceService.ReportgeneratePdf(r,response);
	}
	
	@ApiOperation(value = "Generates Sales Report")
	@RequestMapping(path = "/sales-report/pdf", method = RequestMethod.POST)
	public void addSalesReport(@RequestBody reportForm r, HttpServletResponse response) throws Exception {
		invoiceService.SalesReportgeneratePdf(r,response);
	}
	
	@ApiOperation(value = "Generates Inventory Report")
	@RequestMapping(path = "/inventory-report/pdf", method = RequestMethod.POST)
	public void addInventoryReport(HttpServletResponse response) throws Exception {
		invoiceService.InventoryReportgeneratePdf(response);
	}

 
 }
