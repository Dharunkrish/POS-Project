package com.increff.employee.dao;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.service.ApiException;

	@Repository
	public class orderitemDao extends AbstractDao {

		private Logger logger = Logger.getLogger(orderitemDao.class);

		private static String select_all = "select o from orderPojo o";
        private static String select_prod="select pc from productPojo pc where barcode=:barcode";
        private static String select_inv="select i from inventoryPojo i where id=:id";
		private static String update_inv = "update inventoryPojo pc set pc.quantity=:quantity where id=:id";
		private static String update_orderitem = "update orderitemPojo pc set pc.name=:name, pc.barcode=:barcode , pc.price=:price , pc.quantity=:quantity where id=:id";
		private static String delete_inv = "delete inventoryPojo pc where id=:id";
        private static String select_item="select i from orderitemPojo i where order_id=:id";
        private static String select_id="select i from orderitemPojo i where id=:id";
        private static String select_order_id="select i from orderPojo i where id=:id";



		
		@Transactional
		public void insert(orderitemPojo o) throws ApiException {
				em().persist(o);
		}
		
		public void upd(int quantity,int id) {
			Query query=getQuery(update_inv);
			query.setParameter("quantity",quantity);
			query.setParameter("id", id);
			query.executeUpdate();
		}
		
        public void del_inv(int id) {
        	Query query = em().createQuery(delete_inv);
			query.setParameter("id", id);
			query.executeUpdate();
        }
		
		public int create() {
			orderPojo o=new orderPojo();
			o.setT(ZonedDateTime.now(ZoneId.systemDefault()));
			o.setInvoiceGenerated(false);
			em().persist(o);
			return o.getId();
		}
		
		public productPojo check(String barcode) {
			TypedQuery<productPojo> q= em().createQuery(select_prod,productPojo.class);
			q.setParameter("barcode",barcode);
			return getSingle(q);
		}
	
		public inventoryPojo prodquantity(int id) {
			TypedQuery<inventoryPojo> q=em().createQuery(select_inv,inventoryPojo.class);
			q.setParameter("id",id);
			return getSingle(q);
		}
		public List<orderitemPojo> selectitem(int id) {
			TypedQuery<orderitemPojo> query = getQuery(select_item, orderitemPojo.class);
			query.setParameter("id", id);
			return query.getResultList();
		}
		
		public orderitemPojo select(int id) {
			TypedQuery<orderitemPojo> query = getQuery(select_id, orderitemPojo.class);
			query.setParameter("id", id);
			return getSingle(query);
		}
		
		public orderPojo selectid(int id) {
			TypedQuery<orderPojo> query = getQuery(select_order_id, orderPojo.class);
			query.setParameter("id", id);
			return getSingle(query);
		}
/*
 *  
		public int delete(int product_id) {
			Query query = em().createQuery(delete_id);
			query.setParameter("product_id", product_id);
			return query.executeUpdate();
		}

		
		
		public productDTO selectbar(String barcode) {
			TypedQuery<productDTO> query = getQuery(select_id, productDTO.class);
			query.setParameter("barcode", barcode);
			return getSingle(query);
		}


*/
		public List<orderPojo> selectAll() throws ApiException{
            TypedQuery<orderPojo> query = getQuery(select_all, orderPojo.class);
			List<orderPojo> p= query.getResultList();
			for (orderPojo h:p) {
				logger.info(""+h.getId());
			}
			return p;
		}
		

		public void update(int id,orderPojo o) throws ApiException{
			}
		
		public void update(int id,orderitemPojo o) throws ApiException{
			Query query = getQuery(update_orderitem);
			query.setParameter("name", o.getName());
			query.setParameter("barcode", o.getBarcode());
			query.setParameter("price", o.getPrice());
			query.setParameter("quantity", o.getQuantity());
			query.setParameter("id",id);
			logger.info(query.toString());
			logger.info(query.executeUpdate());
			
		}
			
			

			
		}


	


