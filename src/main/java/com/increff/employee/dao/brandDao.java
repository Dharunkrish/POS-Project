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
		private static String select_category = "select p from brandPojo p where p.category=:category";
		private static String select_all = "select p from brandPojo p";
		private static String select_prod = "SELECT c FROM brandPojo c WHERE c.brand = :brand and c.category= :category";
		private static String update_id = "update brandPojo c set c.brand = :brand, c.category= :category WHERE c.id= :id";
		
		@Transactional
		public void insert(brandPojo p) throws ApiException {
			em().persist(p);
		}


		public brandPojo select(int id) {
			TypedQuery<brandPojo> query = getQuery(select_id, brandPojo.class);
			query.setParameter("id", id);
			return getSingle(query);
		}
		
		public List<brandPojo> getbrand(brandPojo b) {
			TypedQuery<brandPojo> query = getQuery(select_brand, brandPojo.class);
			query.setParameter("brand", b.getBrand());
			return query.getResultList();
		}

		public List<brandPojo> getcategory(brandPojo b) {
			TypedQuery<brandPojo> query = getQuery(select_category, brandPojo.class);
			query.setParameter("category", b.getCategory());
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
			return getSingle(query);
		}

		public void update(brandPojo p) {
			Query query = getQuery(update_id);
			query.setParameter("brand",p.getBrand());
			query.setParameter("category",p.getCategory());
			query.setParameter("id",p.getId());
			query.executeUpdate();
		}


	}


