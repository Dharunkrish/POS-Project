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
		if(StringUtil.isEmpty(p.getBrand())) {
			throw new ApiException("Brand cannot be empty"+p.getBrand()+p.getCategory());
		}
		if(StringUtil.isEmpty(p.getCategory())) {
			throw new ApiException("Category cannot be empty");
		}
		insertCheck(p.getBrand(),p.getCategory());
		dao.insert(p);
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public brandPojo get(int id) throws ApiException {
		logger.info(id);
		return getCheck(id);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<brandPojo> getbrand(String brand) throws ApiException {
		return dao.getbrand(brand);
	}

	@Transactional
	public List<brandPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, brandPojo p) throws ApiException {
		normalize(p);
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
			throw new ApiException("Employee with given ID does not exit, id: " + id);
		}
		return p;
	}
	
	public void insertCheck(String brand,String category) throws ApiException {
		brandPojo p = dao.selectProduct(brand,category);
		if (p!=null) {
			throw new ApiException("Product with given Brand "+p.getBrand()+" and Category "+p.getCategory()+" exist");
		}
		return;
	}
	
	

	protected static void normalize(brandPojo p) {
		p.setBrand(StringUtil.toLowerCase(p.getBrand()));
		p.setCategory(StringUtil.toLowerCase(p.getCategory()));
	}
}
