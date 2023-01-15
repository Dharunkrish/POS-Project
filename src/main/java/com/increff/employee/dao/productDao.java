package com.increff.employee.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.increff.employee.model.productDTO;
import com.increff.employee.pojo.brandPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.service.ApiException;

	@Repository
	public class productDao extends AbstractDao {

		private Logger logger = Logger.getLogger(productDao.class);

		private static String delete_id = "delete from productPojo p where p.product_id=:product_id";
		private static String select_id = "select new com.increff.employee.model.productDTO(pc.name, pc.barcode,pc.mrp,p.id, pc.product_id) from productPojo pc join pc.brand p where pc.product_id=:id";
		private static String update_prod = "update productPojo pc set pc.name=:name, pc.barcode=:barcode,pc.brand=:brand,pc.mrp=:mrp where pc.product_id=:id";
		private static String select_all = "select new com.increff.employee.model.productDTO(pc.name, pc.barcode,pc.mrp,p.id, pc.product_id) from productPojo pc join pc.brand p";
		private static String select_ba = "select new com.increff.employee.model.productDTO(pc.name, pc.barcode,pc.mrp,p.id, pc.product_id) from productPojo pc join pc.brand p where pc.barcode=:barcode";

		
		@Transactional
		public void insert(productPojo p,int id) throws ApiException {
			try {
				brandPojo b = em().getReference(brandPojo.class, id);
				b.product.add(p);
				p.setBrand(b);
				em().persist(p);
			}
			catch(Exception e) {
				throw new ApiException("There is no Brand id that matches your product brand id");
			}
			
		}

		public int delete(int product_id) {
			Query query = em().createQuery(delete_id);
			query.setParameter("product_id", product_id);
			return query.executeUpdate();
		}

		public productDTO select(int id) {
			TypedQuery<productDTO> query = getQuery(select_id, productDTO.class);
			query.setParameter("id", id);
			return getSingle(query);
		}
		
		public productDTO selectbar(String barcode) {
			TypedQuery<productDTO> query = getQuery(select_id, productDTO.class);
			query.setParameter("barcode", barcode);
			return getSingle(query);
		}



		public List<productDTO> selectAll() throws ApiException{
            TypedQuery<productDTO> query = getQuery(select_all, productDTO.class);
			List<productDTO> p= query.getResultList();
			for (productDTO h:p) {
				logger.info(""+h.getBarcode()+h.getMrp()+h.getName()+h.getProduct_id()+h.getBrand_id());
			}
			//throw new ApiException("DF");
			return p;
		}
		
		public brandPojo findid(int id) {
			return em().getReference(brandPojo.class, id);
		}


		public void update(int id,productPojo p) throws ApiException{
			//brandPojo b = em().getReference(brandPojo.class, id);
			try {
			Query query = getQuery(update_prod);
			query.setParameter("name", p.getName());
			query.setParameter("barcode", p.getBarcode());
			query.setParameter("brand", p.getBrand());
			query.setParameter("mrp", p.getMrp());
			query.setParameter("id",id);
			logger.info(query.toString());
			logger.info(query.executeUpdate());
			}
			catch(Exception e) {
				throw new ApiException("There is no Brand id that matches your product brand id");
			}
			
			

			
		}


	}


