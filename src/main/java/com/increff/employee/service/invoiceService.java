package com.increff.employee.service;

import java.io.File;
import java.time.format.DateTimeFormatter;

import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.orderitemDao;
import com.increff.employee.model.DaySalesXml;
import com.increff.employee.model.InventoryReportXml;
import com.increff.employee.model.SalesXmlForm;
import com.increff.employee.model.orderData;
import com.increff.employee.model.orderxmlForm;
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.util.pdfconversionUtil;


@Service
public class invoiceService {

	@Autowired
	private orderitemService orderService;
	private Logger logger = Logger.getLogger(orderitemDao.class);

public byte[] generatePdf(orderxmlForm orderxml) throws Exception{
	orderxmlForm orderInvoiceXmlList = generateInvoiceList(orderxml);
    pdfconversionUtil.generateXml(new File("invoice.xml"), orderInvoiceXmlList, orderxmlForm.class);
    return pdfconversionUtil.generatethePDF(new File("invoice.xml"), new StreamSource("invoice.xsl"));
}

public byte[] ReportgeneratePdf(DaySalesXml daysalesxml) throws Exception{
    pdfconversionUtil.generateXml(new File("daysales.xml"), daysalesxml, DaySalesXml.class);
    return pdfconversionUtil.generatethePDF(new File("daysales.xml"), new StreamSource("daysales.xsl"));
}

public byte[] SalesReportgeneratePdf(SalesXmlForm salesxml) throws Exception{
    pdfconversionUtil.generateXml(new File("sales.xml"), salesxml, SalesXmlForm.class);
    return pdfconversionUtil.generatethePDF(new File("sales.xml"), new StreamSource("sales.xsl"));
}

public byte[] InventoryReportgeneratePdf(InventoryReportXml inventoryxml) throws Exception{
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

public static int calculateTotal(orderxmlForm orderxmlList) {
	int total=0;
	for (orderData o:orderxmlList.getOrderInvoiceData()) {
		total+=(o.getQuantity()*o.getMrp());
	}
	return total;
}
}