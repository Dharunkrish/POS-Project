package com.increff.employee.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.brandDao;
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
	private brandDao bdao;
	
	@Autowired
	private InventoryService s;
	 
	private Logger logger = Logger.getLogger(reportDao.class);

	@Transactional(rollbackOn = ApiException.class)
	public void add() throws ApiException {
		daySalesReportPojo s=new daySalesReportPojo();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ZonedDateTime now = LocalDate.now().atTime(LocalTime.MIN) .atZone(ZoneId.systemDefault());
        now=now.minus(Period.ofDays(1));
        s.setDate(now); 
        s.setTotal_orders(dao.getCount(now));
        daySalesReportForm r = dao.getitemCount(now);
        s.setTotal_items(r.getCount());
        s.setRevenue(r.getRevenue());
        dao.insert(s);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<daySalesReportPojo> get(reportForm s) throws ApiException {
		CheckDateRange(s);
		return dao.get(s);
	}
	
	public Map<String,orderitemPojo> getorder(reportForm s) throws ApiException{
		CheckDateRange(s);
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
	
	public Map<Integer,List<Object>> getsales(reportForm s,Map<String,orderitemPojo> o) throws ApiException {
		logger.info(o.size());
		logger.info(s.getBrand()+s.getCategory()+s.getFrom()+s.getTo());
		List<daysalesData> p=new ArrayList<>();
		if ((s.getBrand()=="" && s.getCategory()=="") || (s.getBrand().equals("none") && s.getCategory().equals("none"))) {
			p= dao.getsales();
		}
		else if (s.getCategory().equals("none")) {
			p= dao.getsaleswb(s);
		}
		else if (s.getBrand().equals("none")) {
			p= dao.getsaleswc(s);
		}
		else {
			if (bdao.selectProduct(s.getBrand(),s.getCategory())==null){
                      throw new ApiException("Brand "+s.getBrand()+" and Category "+s.getCategory()+" combination does not exist");
			}
			p= dao.getsaleswbc(s);
		}
		Map<Integer,List<Object>> m=new HashMap<Integer,List<Object>>();
		for (daysalesData d:p) {
			if (!o.containsKey(d.getBarcode())) {
				continue;
			}
			orderitemPojo oi=o.get(d.getBarcode()); 
		    if (m.containsKey(d.getBrand_Category_id())){
		    	List<Object> ds=m.get(d.getBrand_Category_id());
		    	int i=(Integer) ds.get(0);
		    	i=i+oi.getQuantity();
		    	ds.set(0,i);
		    	double j=(double) ds.get(1);
		    	j=j+(oi.getQuantity()*oi.getPrice());
		    	ds.set(1, j);
			    m.put(d.getBrand_Category_id(), ds);
		    }
		    else {
		    	List<Object> ds=new ArrayList<>();
		    	ds.add(oi.getQuantity());
		    	ds.add(oi.getPrice()*oi.getQuantity());
		    	ds.add(d.getBrand());
		    	ds.add(d.getCategory());
			    m.put(d.getBrand_Category_id(), ds);
		    }
		}
		return m;
	}
	
	public Map<Integer,List<Object>> getinventoryReport(Map<String,inventoryPojo> o) throws ApiException {
		List<daysalesData> p=dao.getsales();
		Map<Integer,List<Object>> m=new HashMap<Integer,List<Object>>();
		for (daysalesData d:p) {
			if (!o.containsKey(d.getBarcode())) {
				continue;
			}
			inventoryPojo oi=o.get(d.getBarcode()); 
		    if (m.containsKey(d.getBrand_Category_id())){
		    	List<Object> ds=m.get(d.getBrand_Category_id());
		    	int i=(Integer) ds.get(0);
		    	i=i+oi.getQuantity();
		    	ds.set(0,i);
			    m.put(d.getBrand_Category_id(), ds);
		    }
		    else {
		    	List<Object> ds=new ArrayList<>();
		    	ds.add(oi.getQuantity());
		    	ds.add(d.getBrand());
		    	ds.add(d.getCategory());
			    m.put(d.getBrand_Category_id(), ds);
		    }
		}
		return m;

		
	}

	protected static void CheckDateRange(reportForm s) throws ApiException{
		if (s.getFrom() == null) {
            throw new ApiException("Please enter valid from date");
        }
        if (s.getTo() == null) {
            throw new ApiException("Please enter valid to date");
        }
       if (ChronoUnit.DAYS.between(s.getFrom(), s.getTo()) <= 0) {
            throw new ApiException("From date is greater than to date");
        }
	}
}
	