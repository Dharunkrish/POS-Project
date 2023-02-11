package com.increff.employee.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
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
import com.increff.employee.model.DaySalesXmlForm;
import com.increff.employee.model.InventoryReportXml;
import com.increff.employee.model.InventoryXmlForm;
import com.increff.employee.model.SalesReportDataXml;
import com.increff.employee.model.SalesXmlForm;
import com.increff.employee.model.orderData;
import com.increff.employee.model.orderxmlForm;
import com.increff.employee.model.reportForm;
import com.increff.employee.pojo.daySalesReportPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.service.invoiceService;
import com.increff.employee.service.orderitemService;
import com.increff.employee.service.reportService;

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

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


	@ApiOperation(value = "Adds an product")
	@RequestMapping(path = "/api/invoice/{id}", method = RequestMethod.GET)
	public void add(@PathVariable int id, HttpServletResponse response) throws Exception {
		List<orderitemPojo> orderList=orderService.get(id);
		List<orderData> d=new ArrayList<>();
		for(orderitemPojo o:orderList) {
		    d.add( convert(o));
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
		List<daySalesReportPojo> d=reportservice.get(r);
	    DaySalesXml x=new DaySalesXml();
		x.setData(convert(d));
		x.setFrom(r.getFrom().format(formatter));
		x.setTo(r.getTo().format(formatter));
		byte bytes[]=invoiceService.ReportgeneratePdf(x);
		createPdfResponse(bytes,response);
	}
	
	@ApiOperation(value = "Generates Sales Report")
	@RequestMapping(path = "/api/sales-report/pdf", method = RequestMethod.POST)
	public void addSalesReport(@RequestBody reportForm r, HttpServletResponse response) throws Exception {
		Map<Integer,List<Object>> d=reportservice.getsales(r,reportservice.getorder(r));
	    SalesXmlForm x=new SalesXmlForm();
		x.setData(convert1(d));
		x.setFrom(r.getFrom().format(formatter));
		x.setTo(r.getTo().format(formatter));
		if ((r.getBrand())=="none" || r.getBrand()=="") {
			x.setBrand("All");
		}
		else {
			x.setBrand(r.getBrand());
		}
		if ((r.getCategory())=="none" || r.getCategory()=="") {
			x.setCategory("All");
		}
		else {
			x.setCategory(r.getCategory());
		}
		byte bytes[]=invoiceService.SalesReportgeneratePdf(x);
		createPdfResponse(bytes,response);
	}
	
	@ApiOperation(value = "Generates Sales Report")
	@RequestMapping(path = "/api/inventory-report/pdf", method = RequestMethod.POST)
	public void addInventoryReport(HttpServletResponse response) throws Exception {
		Map<Integer,List<Object>> d=reportservice.getinventoryReport(reportservice.getinventory());
	    InventoryReportXml x=new InventoryReportXml();
	    x.setData(convert2(d));
		byte bytes[]=invoiceService.InventoryReportgeneratePdf(x);
		createPdfResponse(bytes,response);
	}

	//Creates PDF
    public void createPdfResponse(byte[] bytes, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setContentLength(bytes.length);

        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
    }
 public static orderData convert(orderitemPojo orderitem) {
	orderData d=new orderData();
	d.setName(orderitem.getName());
	d.setQuantity(orderitem.getQuantity());
	d.setMrp(orderitem.getPrice());
	d.setPrice(orderitem.getQuantity()*orderitem.getPrice());
	return d;
 }

 public List<DaySalesXmlForm> convert(List<daySalesReportPojo> ds) {
	List<DaySalesXmlForm> s=new ArrayList<DaySalesXmlForm>();
	for(daySalesReportPojo d:ds){
	DaySalesXmlForm r=new  DaySalesXmlForm();
	r.setDate(formatter.format(d.getDate()));
	r.setTotal_order(d.getTotal_orders());
	r.setTotal_item(d.getTotal_items());
	r.setRevenue(d.getRevenue());
	s.add(r);
	}
	return s;
}
 
public List<SalesReportDataXml> convert1(Map<Integer,List<Object>> m) {
	List<SalesReportDataXml> s=new ArrayList<SalesReportDataXml>();
	for (int b:m.keySet()) {
		List<Object> p=m.get(b);
		SalesReportDataXml r=new SalesReportDataXml();
		r.setQuantity((int) p.get(0));
		r.setRevenue((double) p.get(1));
		r.setBrand((String) p.get(2));
		r.setCategory((String) p.get(3));
		s.add(r);
	 }
		return s;
	}

public List<InventoryXmlForm> convert2(Map<Integer,List<Object>> m) {
	List<InventoryXmlForm> s=new ArrayList<InventoryXmlForm>();
	for (int b:m.keySet()) {
		List<Object> p=m.get(b);
		InventoryXmlForm r=new InventoryXmlForm();
		r.setQuantity((int) p.get(0));
		r.setBrand((String) p.get(1));
		r.setCategory((String) p.get(2));
		s.add(r);
	 }
		return s;
	} 
 }
