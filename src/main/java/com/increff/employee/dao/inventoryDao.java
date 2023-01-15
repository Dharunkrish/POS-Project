package com.increff.employee.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.increff.employee.model.productDTO;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.service.ApiException;

	@Repository
	public class inventoryDao extends AbstractDao {

		private Logger logger = Logger.getLogger(inventoryDao.class);

		private static String select_id = "select pc from inventoryPojo pc where id=:id";
		private static String select_all = "select pc from inventoryPojo pc";
private static String update="update inventoryPojo pc set pc.id=:pid , pc.quantity=:quantity where pc.id=:id";
		private static String select_all_id="select new com.increff.employee.model.productDTO(pc.name, pc.product_id) from productPojo pc where pc.product_id not in (select i.id from inventoryPojo i)";
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
			//throw new ApiException("DF");
			return p;
		}
		
		public List<productDTO> selectid() throws ApiException{

		   List<productDTO> p= em().createQuery(select_all_id).getResultList();
		   for (productDTO l:p) {
			   logger.info("HELLO");
			   logger.info(l.getProduct_id());
		   }
			//throw new ApiException("DF");
			return p;
		}
		
		public productPojo findid(int id) throws ApiException{
		         return em().find(productPojo.class, id);
			   
		}


		public void update(int id,inventoryPojo p) throws ApiException{
			try {
				logger.info(p.getId());
				Query query = getQuery(update);
				query.setParameter("pid", p.getId());
				query.setParameter("quantity", p.getQuantity());
				query.setParameter("id", id);
				logger.info(query.executeUpdate());
				}
				catch(Exception e) {
					throw new ApiException("There is no Brand id that matches your product brand id"+e);
				}
		}


	}


