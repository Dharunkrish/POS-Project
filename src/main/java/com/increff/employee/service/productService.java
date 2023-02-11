package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.productDao;
import com.increff.employee.pojo.brandPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.util.StringUtil;

@Service
public class productService {

	@Autowired
	private productDao dao;


	@Autowired
	private brandService brandService;
	private Logger logger = Logger.getLogger(productDao.class);

	@Transactional(rollbackOn = ApiException.class)
	public void add(productPojo p) throws ApiException {
		normalize(p);
		if (p.getMrp()<0){
			throw new ApiException("MRP cannot be negative"); 
		}
		bar(p.getBarcode(),0);
		brandService.getCheck(p.getBrand_Category_id());
		dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public productPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<productPojo> getAll() throws Exception {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, productPojo p) throws ApiException {
		normalize(p); 
		if (p.getMrp()<0){
			throw new ApiException("MRP cannot be negative"); 
		}  
		bar(p.getBarcode(),id);
		brandService.getCheck(p.getBrand_Category_id());
		productPojo p1=getCheck(id);
		p1.setBarcode(p.getBarcode());
		p1.setBrand_Category_id(p.getBrand_Category_id());
		p1.setMrp(p.getMrp());
		p1.setName(p.getName());
		dao.update(p1);

	}

	@Transactional
	public productPojo getCheck(int id) throws ApiException {
		productPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Product with given ID does not exist, id: " + id);
		}
		return p;
	}
	
	
	
	public void bar(String barcode,int id) throws ApiException{
		productPojo p =dao.selectbar(barcode,id);
		if(p!=null) {
			throw new ApiException("Barcode must be unique");
		}
	}
	

	protected static void normalize(productPojo p) {
		p.setName(StringUtil.toLowerCase(p.getName()));
		p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
	}
}
