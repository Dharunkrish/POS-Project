package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.controller.productApiController;
import com.increff.employee.dao.brandDao;
import com.increff.employee.pojo.brandPojo;
import com.increff.employee.util.StringUtil;

@Service
public class brandService {

	@Autowired
	private brandDao dao;
	private Logger logger = Logger.getLogger(productApiController.class);

	@Transactional(rollbackOn = ApiException.class)
	public void add(brandPojo p) throws ApiException {
		normalize(p);
		insertCheck(p.getBrand(),p.getCategory());
		dao.insert(p);
	}


	@Transactional(rollbackOn = ApiException.class)
	public brandPojo get(int id) throws ApiException {
		logger.info(id);
		return getCheck(id);
	}
	


	@Transactional
	public List<brandPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, brandPojo p) throws ApiException {
		normalize(p);
		logger.info(p.getBrand());
		insertCheck(p.getBrand(),p.getCategory());
		brandPojo ex = getCheck(id);
		ex.setBrand(p.getBrand());
		ex.setCategory(p.getCategory());
		dao.update(p);
	}

	@Transactional
	public brandPojo getCheck(int id) throws ApiException {
		brandPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Brand and Category with given ID does not exist, id: " + id);
		}
		return p;
	}
	
	public void insertCheck(String brand,String category) throws ApiException {
		brandPojo p = dao.selectProduct(brand,category);
		if (p!=null) {
			throw new ApiException("Brand "+p.getBrand()+" and Category "+p.getCategory()+" combination already exist");
		}
		return;
	}
	
	

	protected static void normalize(brandPojo p) {
		p.setBrand(StringUtil.toLowerCase(p.getBrand()));
		p.setCategory(StringUtil.toLowerCase(p.getCategory()));
	}
}
