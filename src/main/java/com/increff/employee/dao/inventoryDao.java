package com.increff.employee.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.service.ApiException;

	@Repository
	public class inventoryDao extends AbstractDao {

		private Logger logger = Logger.getLogger(inventoryDao.class);

		private static String update_inv = "update inventoryPojo pc set pc.quantity=:quantity where pc.barcode=:barcode";
		private static String get_inv = "select pc from inventoryPojo pc where pc.barcode=:barcode";
		private static String select_id = "select pc from inventoryPojo pc where id=:id";
		private static String select_all = "select pc from inventoryPojo pc";
        private static String update="update inventoryPojo pc set pc.quantity=:quantity where pc.id=:id";
		private static String select_all_id="select pc from productPojo pc where pc.product_id not in (select i.id from inventoryPojo i)";
		@Transactional
		public void insert(inventoryPojo p) throws ApiException {
				em().persist(p);	
		}


		public inventoryPojo select(int id) {
			TypedQuery<inventoryPojo> query = getQuery(select_id, inventoryPojo.class);
			query.setParameter("id", id);
			return getSingle(query);
		}



		public List<inventoryPojo> selectAll() throws ApiException{
            TypedQuery<inventoryPojo> query = getQuery(select_all, inventoryPojo.class);
			List<inventoryPojo> p= query.getResultList();
			return p;
		}
		
		public List<productPojo> selectid() throws ApiException{
		   List<productPojo> p= em().createQuery(select_all_id).getResultList();
			return p;
		}
		
		public productPojo findid(int id) throws ApiException{
		         return em().find(productPojo.class, id);
			   
		}


		
		public void update(inventoryPojo p) throws ApiException{
		}


	}


