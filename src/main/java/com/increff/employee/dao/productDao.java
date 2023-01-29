package com.increff.employee.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.brandPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.service.ApiException;

	@Repository
	public class productDao extends AbstractDao {

		private Logger logger = Logger.getLogger(productDao.class);

		private static String delete_id = "delete from productPojo p where p.product_id=:product_id";
		private static String select_id = "select pc from productPojo pc where pc.product_id=:id";
		private static String update_prod = "update productPojo pc set pc.name=:name, pc.barcode=:barcode,pc.brand_Category=:brand,pc.mrp=:mrp where pc.product_id=:id";
		private static String select_all = "select pc from productPojo pc";
		//private static String select_ba = "select new com.increff.employee.model.productDTO(pc.name, pc.barcode,pc.mrp,p.id, pc.product_id) from productPojo pc join pc.brand p where pc.barcode=:barcode";
		private static String select_brand = "select b from brandPojo b where b.brand=:brand and b.category=:category";
		private static String select_bar = "select pc from productPojo pc where pc.barcode=:barcode and product_id!=:id";

		
		@Transactional
		public void insert(productPojo p) throws ApiException {
				em().persist(p);	
		}

		public int delete(int product_id) {
			Query query = em().createQuery(delete_id);
			query.setParameter("product_id", product_id);
			return query.executeUpdate();
		}

		public productPojo select(int id) {
			TypedQuery<productPojo> query = getQuery(select_id, productPojo.class);
			query.setParameter("id", id);
			return getSingle(query);
		}
		
		public productPojo selectbar(String barcode,int id) {
			TypedQuery<productPojo> query = getQuery(select_bar, productPojo.class);
			query.setParameter("barcode", barcode);
           logger.info(id);
			query.setParameter("id", id);
			return getSingle(query);
		}



		public List<productPojo> selectAll() throws ApiException{
            TypedQuery<productPojo> query = getQuery(select_all, productPojo.class);
			List<productPojo> p= query.getResultList();
			for (productPojo h:p) {
				logger.info(""+h.getBarcode()+h.getMrp()+h.getName()+h.getProduct_id()+h.getBrand_Category());
			}
			return p;
		}
		
		public brandPojo findbrand(String brand,String category) {
			TypedQuery<brandPojo> query = getQuery(select_brand, brandPojo.class);
			query.setParameter("brand",brand );
			query.setParameter("category",category );
			return getSingle(query);
		}


		public void update(int id,productPojo p) throws ApiException{
			Query query = getQuery(update_prod);
			query.setParameter("name", p.getName());
			query.setParameter("barcode", p.getBarcode());
			query.setParameter("brand", p.getBrand_Category());
			query.setParameter("mrp", p.getMrp());
			query.setParameter("id",id);
			logger.info(query.toString());
			logger.info(query.executeUpdate());	
		}


	}


