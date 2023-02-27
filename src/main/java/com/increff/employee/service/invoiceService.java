package com.increff.employee.service;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.orderitemDao;
import com.increff.employee.dao.reportDao;
import com.increff.employee.model.Form.reportForm;
import com.increff.employee.model.Xml.DaySalesXml;
import com.increff.employee.model.Xml.DaySalesXmlForm;
import com.increff.employee.model.Xml.InventoryReportXml;
import com.increff.employee.model.Xml.InventoryXmlForm;
import com.increff.employee.model.Xml.SalesReportDataXml;
import com.increff.employee.model.Xml.SalesXmlForm;
import com.increff.employee.model.Xml.orderData;
import com.increff.employee.model.Xml.orderxmlForm;
import com.increff.employee.pojo.daySalesReportPojo;
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.util.DataConversionUtil;
import com.increff.employee.util.pdfconversionUtil;


@Service
public class invoiceService {

	@Autowired
	private orderitemService orderService;

    @Autowired
	private reportService reportservice;
    
    @Autowired
	private reportDao reportdao;

	private Logger logger = Logger.getLogger(orderitemDao.class);
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


public void generatePdf(int id,HttpServletResponse response) throws Exception{
	List<orderitemPojo> orderList=orderService.get(id);
	List<orderData> d=new ArrayList<>();
	for(orderitemPojo o:orderList) {
	    d.add(DataConversionUtil.convert(o));
	}
	orderxmlForm invoicexml=new orderxmlForm();
	invoicexml.setOrder_id(id);
	invoicexml.setOrderInvoiceData(d);
	orderxmlForm orderInvoiceXmlList = generateInvoiceList(invoicexml);
    pdfconversionUtil.generateXml(new File("invoice.xml"), orderInvoiceXmlList, orderxmlForm.class);
    byte[] bytes= pdfconversionUtil.generatethePDF(new File("invoice.xml"), new StreamSource("invoice.xsl"));
	createPdfResponse(bytes,response);
}

public void ReportgeneratePdf(reportForm r,HttpServletResponse response) throws Exception{
    DaySalesXml daysalesxml=generateDaySalesReport(r);
    pdfconversionUtil.generateXml(new File("daysales.xml"), daysalesxml, DaySalesXml.class);
    byte[] bytes= pdfconversionUtil.generatethePDF(new File("daysales.xml"), new StreamSource("daysales.xsl"));
	createPdfResponse(bytes,response);
}

public void SalesReportgeneratePdf(reportForm r,HttpServletResponse response) throws Exception{
    SalesXmlForm salesxml=generateSalesReport(r);
    pdfconversionUtil.generateXml(new File("sales.xml"), salesxml, SalesXmlForm.class);
    byte[] bytes= pdfconversionUtil.generatethePDF(new File("sales.xml"), new StreamSource("sales.xsl"));
	createPdfResponse(bytes,response);
}

public void InventoryReportgeneratePdf(HttpServletResponse response) throws Exception{
    InventoryReportXml inventoryxml=generateInventoryReport();
    pdfconversionUtil.generateXml(new File("inventoryXml.xml"), inventoryxml, InventoryReportXml.class);
    byte[] bytes= pdfconversionUtil.generatethePDF(new File("inventoryXml.xml"), new StreamSource("inventoryXml.xsl"));
	createPdfResponse(bytes,response);
}

public orderxmlForm generateInvoiceList(orderxmlForm orderxmlList) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        orderPojo order=orderService.getorder(orderxmlList.getOrder_id());
        orderxmlList.setDatetime(order.getT().format(formatter));
        double total = calculateTotal(orderxmlList);
        orderxmlList.setTotal(total);
        order.setInvoiceGenerated(true);
        orderService.update(order.getId(),order);
        return orderxmlList;
    }

    public DaySalesXml generateDaySalesReport(reportForm r) throws Exception {
        List<daySalesReportPojo> d=reportdao.get(r);
	    DaySalesXml x=new DaySalesXml();
		x.setData(DataConversionUtil.convert(d));
		x.setFrom(r.getFrom().format(formatter));
		x.setTo(r.getTo().format(formatter));
		List<Object> m=calculateDaySalesTotal(x.getData());
		x.setTotal_order((int)m.get(0));
		x.setRevenue((double)m.get(1));
		x.setTotal_item((int)m.get(2));
        return x;
    }

    public SalesXmlForm generateSalesReport(reportForm r) throws Exception {
	    SalesXmlForm x=new SalesXmlForm();
		x.setData(DataConversionUtil.convert4(reportservice.getsales(r)));
		x.setFrom(r.getFrom().format(formatter));
		x.setTo(r.getTo().format(formatter));
		if ((r.getBrand()).equals("none") || r.getBrand().isEmpty()) {
			logger.info("j");
			x.setBrand("All");
		}
		else {
			x.setBrand(r.getBrand());
		}
		if ((r.getCategory()).equals("none") || r.getCategory().isEmpty()) {
			x.setCategory("All");
		}
		else {
			x.setCategory(r.getCategory());
		}
		List<Object> total=calculateSalesTotal(x.getData());
		x.setTotal_quantity((int)total.get(0));
		x.setRevenue((double)total.get(1));
        return x;
    }

    public InventoryReportXml generateInventoryReport() throws Exception {
	    InventoryReportXml x=new InventoryReportXml();
	    x.setData(DataConversionUtil.convert3(reportservice.getinventoryReport()));
        x.setTotal_quantity(calculateInventoryTotal(x.getData()));
        return x;
    }

public static int calculateTotal(orderxmlForm orderxmlList) {
	int total=0;
	for (orderData o:orderxmlList.getOrderInvoiceData()) {
		total+=(o.getQuantity()*o.getMrp());
	}
	return total;
}

public static int calculateInventoryTotal(List<InventoryXmlForm> inventoryxmlList) {
	int total=0;
	for (InventoryXmlForm o:inventoryxmlList) {
		total+=(o.getQuantity());
	}
	return total;         
}

public static List<Object> calculateSalesTotal(List<SalesReportDataXml> salesxmlList) {
	int total1=0;
	double total2=0;
	List<Object> total=new ArrayList<>();
	for (SalesReportDataXml o:salesxmlList) {
		total1+=(o.getQuantity());
		total2+=(o.getRevenue());
	}
	total.add(total1);
	total.add(total2);
	return total;
}

public static List<Object> calculateDaySalesTotal(List<DaySalesXmlForm> salesxmlList) {
	int total1=0;
	int total3=0;
	double total2=0;
	List<Object> total=new ArrayList<>();
	for (DaySalesXmlForm o:salesxmlList) {
		total1+=(o.getTotal_order());
		total2+=(o.getRevenue());
		total3+=(o.getTotal_item());
	}
	total.add(total1);
	total.add(total2);
	total.add(total3);
	return total;
}

//Creates PDF
public void createPdfResponse(byte[] bytes, HttpServletResponse response) throws IOException {
    response.setContentType("application/pdf");
    response.setContentLength(bytes.length);
    response.getOutputStream().write(bytes);
    response.getOutputStream().flush();
}

}