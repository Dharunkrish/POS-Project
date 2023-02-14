package com.increff.employee.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.DaySalesXml;
import com.increff.employee.model.InventoryReportXml;
import com.increff.employee.model.SalesXmlForm;
import com.increff.employee.model.orderData;
import com.increff.employee.model.orderxmlForm;
import com.increff.employee.model.reportForm;
import com.increff.employee.pojo.daySalesReportPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.service.invoiceService;
import com.increff.employee.service.orderitemService;
import com.increff.employee.service.reportService;
import com.increff.employee.util.DataConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InvoiceApiController {

	@Autowired
	private orderitemService orderService;
	@Autowired
	private invoiceService invoiceService;
	@Autowired
	private reportService reportservice;
   
	private Logger logger = Logger.getLogger(InvoiceApiController.class);



	@ApiOperation(value = "Adds an product")
	@RequestMapping(path = "/api/invoice/{id}", method = RequestMethod.GET)
	public void add(@PathVariable int id, HttpServletResponse response) throws Exception {
		List<orderitemPojo> orderList=orderService.get(id);
		List<orderData> d=new ArrayList<>();
		for(orderitemPojo o:orderList) {
		    d.add(DataConversionUtil.convert(o));
		}
		orderxmlForm invoicexml=new orderxmlForm();
		invoicexml.setOrder_id(id);
		invoicexml.setOrderInvoiceData(d);
		byte bytes[]=invoiceService.generatePdf(invoicexml);
		createPdfResponse(bytes,response);
	}
	
	@ApiOperation(value = "Generates Daily Sales Report")
	@RequestMapping(path = "/api/report/pdf", method = RequestMethod.POST)
	public void addReport(@RequestBody reportForm r, HttpServletResponse response) throws Exception {
		byte bytes[]=invoiceService.ReportgeneratePdf(r);
		createPdfResponse(bytes,response);
	}
	
	@ApiOperation(value = "Generates Sales Report")
	@RequestMapping(path = "/api/sales-report/pdf", method = RequestMethod.POST)
	public void addSalesReport(@RequestBody reportForm r, HttpServletResponse response) throws Exception {
		byte bytes[]=invoiceService.SalesReportgeneratePdf(r);
		createPdfResponse(bytes,response);
	}
	
	@ApiOperation(value = "Generates Sales Report")
	@RequestMapping(path = "/api/inventory-report/pdf", method = RequestMethod.POST)
	public void addInventoryReport(HttpServletResponse response) throws Exception {
		byte bytes[]=invoiceService.InventoryReportgeneratePdf();
		createPdfResponse(bytes,response);
	}

	//Creates PDF
    public void createPdfResponse(byte[] bytes, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
    }
 
 }
