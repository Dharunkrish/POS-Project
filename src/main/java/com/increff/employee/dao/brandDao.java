package com.increff.employee.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.increff.employee.controller.productApiController;
import com.increff.employee.pojo.brandPojo;
import com.increff.employee.service.ApiException;

	@Repository
	public class brandDao extends AbstractDao {

		private Logger logger = Logger.getLogger(productApiController.class);

		private static String select_id = "select p from brandPojo p where p.id=:id";
		private static String select_brand = "select p from brandPojo p where p.brand=:brand";
		private static String select_all = "select p from brandPojo p";
		private static String select_prod = "SELECT c FROM brandPojo c WHERE c.brand = :brand and c.category= :category";


		

		
		@Transactional
		public void insert(brandPojo p) throws ApiException {
			em().persist(p);
		}

		public void delete(int id) {
			brandPojo brand=select(id);
			logger.info(brand.getBrand());
			em().remove(brand);
		}

		public brandPojo select(int id) {
			logger.info("df"+id);
			TypedQuery<brandPojo> query = getQuery(select_id, brandPojo.class);
			query.setParameter("id", id);
			logger.info(id);
			return getSingle(query);
		}

		public List<brandPojo> getbrand(String brand) {
			TypedQuery<brandPojo> query = getQuery(select_brand, brandPojo.class);
			query.setParameter("brand", brand);
			return query.getResultList();
		}

		public List<brandPojo> selectAll() {
			TypedQuery<brandPojo> query = getQuery(select_all, brandPojo.class);
			return query.getResultList();
		}
		
		public brandPojo selectProduct(String brand,String category) throws ApiException {
			TypedQuery<brandPojo> query = getQuery(select_prod, brandPojo.class);
			query.setParameter("brand", brand);
			query.setParameter("category", category);
			//List<brandPojo>a= query.getResultList();
			return getSingle(query);
		}

		public void update(brandPojo p) {
			
		}


	}


