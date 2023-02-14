package com.increff.employee.service;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.orderitemDao;
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
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.util.DataConversionUtil;
import com.increff.employee.util.pdfconversionUtil;


@Service
public class invoiceService {

	@Autowired
	private orderitemService orderService;

    @Autowired
	private reportService reportservice;

	private Logger logger = Logger.getLogger(orderitemDao.class);
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


public byte[] generatePdf(orderxmlForm orderxml) throws Exception{
	orderxmlForm orderInvoiceXmlList = generateInvoiceList(orderxml);
    pdfconversionUtil.generateXml(new File("invoice.xml"), orderInvoiceXmlList, orderxmlForm.class);
    return pdfconversionUtil.generatethePDF(new File("invoice.xml"), new StreamSource("invoice.xsl"));
}

public byte[] ReportgeneratePdf(reportForm r) throws Exception{
    DaySalesXml daysalesxml=generateDaySalesReport(r);
    pdfconversionUtil.generateXml(new File("daysales.xml"), daysalesxml, DaySalesXml.class);
    return pdfconversionUtil.generatethePDF(new File("daysales.xml"), new StreamSource("daysales.xsl"));
}

public byte[] SalesReportgeneratePdf(reportForm r) throws Exception{
    SalesXmlForm salesxml=generateSalesReport(r);
    pdfconversionUtil.generateXml(new File("sales.xml"), salesxml, SalesXmlForm.class);
    return pdfconversionUtil.generatethePDF(new File("sales.xml"), new StreamSource("sales.xsl"));
}

public byte[] InventoryReportgeneratePdf() throws Exception{
    InventoryReportXml inventoryxml=generateInventoryReport();
    pdfconversionUtil.generateXml(new File("inventoryXml.xml"), inventoryxml, InventoryReportXml.class);
    return pdfconversionUtil.generatethePDF(new File("inventoryXml.xml"), new StreamSource("inventoryXml.xsl"));
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
        List<daySalesReportPojo> d=reportservice.get(r);
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
        Map<Integer,List<Object>> d=reportservice.getsales(r,reportservice.getorder(r));
	    SalesXmlForm x=new SalesXmlForm();
		x.setData(DataConversionUtil.convert1(d));
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
        Map<Integer,List<Object>> d=reportservice.getinventoryReport(reportservice.getinventory());
	    InventoryReportXml x=new InventoryReportXml();
	    x.setData(DataConversionUtil.convert2(d));
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
}