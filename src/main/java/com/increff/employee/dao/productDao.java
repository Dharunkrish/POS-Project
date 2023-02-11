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
		private static String update_prod = "update productPojo pc set pc.name=:name, pc.barcode=:barcode,pc.brand_Category_id=:brand,pc.mrp=:mrp where pc.product_id=:id";
		private static String select_all = "select pc from productPojo pc";
		private static String select_brand = "select b from brandPojo b where b.brand=:brand and b.category=:category";
		private static String select_bar = "select pc from productPojo pc where pc.barcode=:barcode and product_id!=:id";

		
		@Transactional
		public void insert(productPojo p) throws ApiException {
				em().persist(p);	
		}


		public productPojo select(int id) {
			TypedQuery<productPojo> query = getQuery(select_id, productPojo.class);
			query.setParameter("id", id);
			return getSingle(query);
		}
		
		public productPojo selectbar(String barcode,int id) {
			TypedQuery<productPojo> query = getQuery(select_bar, productPojo.class);
			query.setParameter("barcode", barcode);
			query.setParameter("id", id);
			return getSingle(query);
		}



		public List<productPojo> selectAll() throws ApiException{
            TypedQuery<productPojo> query = getQuery(select_all, productPojo.class);
			List<productPojo> p= query.getResultList();
			return p;
		}
		
		/*public brandPojo findbrand(String brand,String category) {
			TypedQuery<brandPojo> query = getQuery(select_brand, brandPojo.class);
			query.setParameter("brand",brand );
			query.setParameter("category",category );
			return getSingle(query);
		}*/


		public void update(productPojo p) throws ApiException{
		}

	}


