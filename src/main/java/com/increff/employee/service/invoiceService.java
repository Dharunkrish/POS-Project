package com.increff.employee.service;

import java.io.File;
import java.time.format.DateTimeFormatter;

import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.orderitemDao;
import com.increff.employee.model.orderData;
import com.increff.employee.model.orderxmlForm;
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.util.pdfconversionUtil;

@Service
public class invoiceService {

	@Autowired
	private orderitemService orderService;
	private pdfconversionUtil util;
	private Logger logger = Logger.getLogger(orderitemDao.class);

public String hello() {
	return "HEllo World";
}
public byte[] generatePdf(orderxmlForm orderxml) throws Exception{
	logger.info("order");
	orderxmlForm orderInvoiceXmlList = generateInvoiceList(orderxml);
    pdfconversionUtil.generateXml(new File("invoice.xml"), orderInvoiceXmlList, orderxmlForm.class);
    return pdfconversionUtil.generatethePDF(new File("invoice.xml"), new StreamSource("invoice.xsl"));
}

public orderxmlForm generateInvoiceList(orderxmlForm orderxmlList) throws Exception {
	    logger.info("k");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        orderPojo order=orderService.getorder(orderxmlList.getOrder_id());
        orderxmlList.setDatetime(order.getT().format(formatter));
        double total = calculateTotal(orderxmlList);
        logger.info(total);
        orderxmlList.setTotal(total);
        logger.info(orderxmlList.getTotal());
        order.setInvoiceGenerated(true);
        orderService.update(order.getId(),order);
        logger.info(orderxmlList.getTotal());
        for (orderData x:orderxmlList.getOrderInvoiceData()) {
        	logger.info(x.getName());
        }
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