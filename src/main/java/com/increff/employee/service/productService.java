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
	private Logger logger = Logger.getLogger(productDao.class);

	@Transactional(rollbackOn = ApiException.class)
	public void add(productPojo p) throws ApiException {
		normalize(p);
		bar(p.getBarcode(),0);
		dao.insert(p);
	}

	@Transactional
	public void delete(int product_id) {
		dao.delete(product_id);
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
		bar(p.getBarcode(),id);
		dao.update(id,p);
	}

	@Transactional
	public productPojo getCheck(int id) throws ApiException {
		productPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Product with given ID does not exit, id: " + id);
		}
		return p;
	}
	
	
	
	public void bar(String barcode,int id) throws ApiException{
		productPojo p =dao.selectbar(barcode,id);
		if(p!=null) {
			throw new ApiException("Barcode must be unique");
		}
	}
	
	private void insertCheck(String brand,String category) throws ApiException {
		brandPojo b =dao.findbrand(brand,category);
		if (b==null) {
			throw new ApiException("Brand and Category Does not exist");
		}
	}
	

	protected static void normalize(productPojo p) {
		p.setName(StringUtil.toLowerCase(p.getName()));
		p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
	}
}
