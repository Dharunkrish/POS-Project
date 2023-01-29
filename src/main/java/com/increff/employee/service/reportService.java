package com.increff.employee.service;

import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.reportDao;
import com.increff.employee.model.daySalesReportForm;
import com.increff.employee.model.daysalesData;
import com.increff.employee.model.reportForm;
import com.increff.employee.pojo.daySalesReportPojo;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.orderitemPojo;

@Service
public class reportService {

	@Autowired
	private reportDao dao;
	
	@Autowired
	private InventoryService s;
	 
	private Logger logger = Logger.getLogger(reportDao.class);

	@Transactional(rollbackOn = ApiException.class)
	public void add() throws ApiException {
		daySalesReportPojo s=new daySalesReportPojo();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ZonedDateTime now = ZonedDateTime.now();
        now=now.minus(Period.ofDays(1));
        logger.info(formatter.format(now));
        s.setDate(now); 
        s.setTotal_orders(dao.getCount(now));
        logger.info(s.getTotal_orders());
        daySalesReportForm r = dao.getitemCount(now);
        s.setTotal_items(r.getCount());
        s.setRevenue(r.getRevenue());
        logger.info(s.getTotal_items());
        logger.info(s.getRevenue());
        dao.insert(s);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<daySalesReportPojo> get(reportForm s) throws ApiException {
		return dao.get(s);
	}
	
	public Map<String,orderitemPojo> getorder(reportForm s) throws ApiException{
		List<orderitemPojo> o= dao.getOrder(s.getFrom(),s.getTo());
		Map<String ,orderitemPojo> m=new HashMap<String,orderitemPojo>();
		for (orderitemPojo oi:o) {
			m.put(oi.getBarcode(), oi);	
		}
       return m;
	}
	
	public Map<String,inventoryPojo> getinventory() throws Exception{
		List<inventoryPojo> o= s.getAll();
		Map<String ,inventoryPojo> m=new HashMap<String,inventoryPojo>();
		for (inventoryPojo oi:o) {
			m.put(oi.getBarcode(), oi);	
		}
       return m;
	}
	
	public Map<String,List<Object>> getsales(reportForm s,Map<String,orderitemPojo> o) throws ApiException {
		List<daysalesData> p=new ArrayList<>();
		logger.info(s.getCategory());
		if (s.getBrand()=="") {
			p= dao.getsales();
		}
		else if (s.getCategory().equals("none")) {
			logger.info("K");
			p= dao.getsaleswb(s);
		}
		else {
			p= dao.getsaleswbc(s);
		}
		logger.info("K");
		Map<String,List<Object>> m=new HashMap<String,List<Object>>();
		for (daysalesData d:p) {
			if (!o.containsKey(d.getBarcode())) {
				continue;
			}
			orderitemPojo oi=o.get(d.getBarcode()); 
			logger.info(oi.getQuantity());
		    if (m.containsKey(d.getBrand_category())){
		    	logger.info(d.getBarcode());
		    	List<Object> ds=m.get(d.getBrand_category());
		    	int i=(Integer) ds.get(0);
		    	logger.info(oi.getQuantity());
		    	i=i+oi.getQuantity();
		    	logger.info(i);
		    	ds.set(0,i);
		    	logger.info(ds.get(1));
		    	double j=(double) ds.get(1);
		    	j=j+(oi.getQuantity()*oi.getPrice());
		    	ds.set(1, j);
		    	logger.info(j);
			    m.put(d.getBrand_category(), ds);
		    }
		    else {
		    	List<Object> ds=new ArrayList<>();
		    	ds.add(oi.getQuantity());
		    	ds.add(oi.getPrice()*oi.getQuantity());
		    	ds.add(d.getBrand());
		    	ds.add(d.getCategory());
		    	logger.info(d.getBarcode());
			    m.put(d.getBrand_category(), ds);
		    }
		}
		logger.info("k");
		return m;
	}
	
	public Map<String,List<Object>> getinventoryReport(Map<String,inventoryPojo> o) throws ApiException {
		List<daysalesData> p=dao.getsales();
		Map<String,List<Object>> m=new HashMap<String,List<Object>>();
		for (daysalesData d:p) {
			if (!o.containsKey(d.getBarcode())) {
				continue;
			}
			inventoryPojo oi=o.get(d.getBarcode()); 
			logger.info(oi.getQuantity());
		    if (m.containsKey(d.getBrand_category())){
		    	logger.info(d.getBarcode());
		    	List<Object> ds=m.get(d.getBrand_category());
		    	int i=(Integer) ds.get(0);
		    	logger.info(oi.getQuantity());
		    	i=i+oi.getQuantity();
		    	logger.info(i);
		    	ds.set(0,i);
			    m.put(d.getBrand_category(), ds);
		    }
		    else {
		    	List<Object> ds=new ArrayList<>();
		    	ds.add(oi.getQuantity());
		    	ds.add(d.getBrand());
		    	ds.add(d.getCategory());
		    	logger.info(d.getBarcode());
			    m.put(d.getBrand_category(), ds);
		    }
		}
		return m;

		
	}
}
	