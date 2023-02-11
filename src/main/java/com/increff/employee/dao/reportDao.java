package com.increff.employee.dao;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.increff.employee.model.daySalesReportForm;
import com.increff.employee.model.daysalesData;
import com.increff.employee.model.reportForm;
import com.increff.employee.pojo.daySalesReportPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.service.ApiException;

	@Repository
	public class reportDao extends AbstractDao {

		private Logger logger = Logger.getLogger(productDao.class);

		private static String cal_item = "select new com.increff.employee.model.daySalesReportForm(sum(oi.quantity*oi.price),count(oi.id)) from orderitemPojo oi inner join orderPojo o on oi.order_id=o.id where o.t>=:date1 and o.t<=:date2";
		private static String sel_ord = "select oi from orderitemPojo oi inner join orderPojo o on oi.order_id=o.id where o.t>=:date1 and o.t<=:date2";
		private static String cal_count = "select count(pc.id) from orderPojo pc where pc.t>=:date1 and pc.t<=:date2";
		private static String select_day = "select d from daySalesReportPojo d where d.date>=:date1 and d.date<=:date2";
		private static String select_order = "select  new com.increff.employee.model.daysalesData(b.brand,b.category,p.barcode,p.brand_Category_id) from productPojo p join brandPojo b on p.brand_Category_id=b.id";
		private static String select_orderwbc = "select  new com.increff.employee.model.daysalesData(b.brand,b.category,p.barcode,p.brand_Category_id) from productPojo p join brandPojo b on p.brand_Category_id=b.id where b.brand=:brand and b.category=:category";
		private static String select_orderwb = "select  new com.increff.employee.model.daysalesData(b.brand,b.category,p.barcode,p.brand_Category_id) from productPojo p join brandPojo b on p.brand_Category_id=b.id where b.brand=:brand";
		private static String select_orderwc = "select  new com.increff.employee.model.daysalesData(b.brand,b.category,p.barcode,p.brand_Category_id) from productPojo p join brandPojo b on p.brand_Category_id=b.id where b.category=:category";


		@Transactional 
		public int getCount(ZonedDateTime d) {
		    Query query= em().createQuery(cal_count);
		    query.setParameter("date1",d);
		    query.setParameter("date2",d.plusDays(1));
		    int count=(int)((long) query.getSingleResult());
			return count;
		}
		
		@Transactional 
		public daySalesReportForm getitemCount(ZonedDateTime d) {
		    TypedQuery<daySalesReportForm> query = getQuery(cal_item, daySalesReportForm.class);
		    query.setParameter("date1",d);
		    query.setParameter("date2",d.plusDays(1));
		    daySalesReportForm r= query.getSingleResult();
			return r;
		}
		
		@Transactional
		public void insert(daySalesReportPojo p) throws ApiException {
				em().persist(p);	
		}
		
		@Transactional
		public List<daySalesReportPojo> get(reportForm r) throws ApiException {
			 TypedQuery<daySalesReportPojo> query = getQuery(select_day, daySalesReportPojo.class);
			    query.setParameter("date1",r.getFrom());
			    query.setParameter("date2",r.getTo().plusDays(1));
			    List<daySalesReportPojo> s= query.getResultList();
			    return s;
		}
		
		
		@Transactional
		public List<orderitemPojo> getOrder(ZonedDateTime date1,ZonedDateTime date2) throws ApiException {
			 TypedQuery<orderitemPojo> query = getQuery(sel_ord, orderitemPojo.class);
			    query.setParameter("date1",date1);
			    query.setParameter("date2",date2.plusDays(1));
			    return query.getResultList();
		}
		
		@Transactional
		public List<orderitemPojo> getproduct(reportForm r) throws ApiException {
			 TypedQuery<orderitemPojo> query = getQuery(select_order, orderitemPojo.class);
			    query.setParameter("date1",r.getFrom());
			    query.setParameter("date2",r.getTo());
			    return query.getResultList();
		}
		
		@Transactional
		public List<daysalesData> getsaleswb(reportForm r) throws ApiException {
			 TypedQuery<daysalesData> query = getQuery(select_orderwb, daysalesData.class);
			    query.setParameter("brand",r.getBrand());
			    return query.getResultList();
		}
		
		@Transactional
		public List<daysalesData> getsaleswc(reportForm r) throws ApiException {
			 TypedQuery<daysalesData> query = getQuery(select_orderwc, daysalesData.class);
			    query.setParameter("category",r.getCategory());
			    return query.getResultList();
		}
		
		@Transactional
		public List<daysalesData> getsales() throws ApiException {
			 TypedQuery<daysalesData> query = getQuery(select_order, daysalesData.class);
			    return query.getResultList();
		}
		
		@Transactional
		public List<daysalesData> getinventory() throws ApiException {
			 TypedQuery<daysalesData> query = getQuery(select_order, daysalesData.class);
			    return query.getResultList();
		}
		
		@Transactional
		public List<daysalesData> getsaleswbc(reportForm r) throws ApiException {
			 TypedQuery<daysalesData> query = getQuery(select_orderwbc, daysalesData.class);
			    query.setParameter("brand",r.getBrand());
			    query.setParameter("category",r.getCategory());
			    return query.getResultList();
		}
		
		
}
	