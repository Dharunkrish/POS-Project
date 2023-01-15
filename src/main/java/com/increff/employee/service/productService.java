package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.productDao;
import com.increff.employee.model.productDTO;
import com.increff.employee.pojo.brandPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.util.StringUtil;

@Service
public class productService {

	@Autowired
	private productDao dao;
	private Logger logger = Logger.getLogger(productDao.class);

	@Transactional(rollbackOn = ApiException.class)
	public void add(productPojo p,int id) throws ApiException {
		normalize(p);
		if(StringUtil.isEmpty(p.getName())) {
			throw new ApiException("Name cannot be empty");
		}
		if(StringUtil.isEmpty(p.getBarcode())) {
			throw new ApiException("Barcode cannot be empty");
		}
		if (p.getMrp()==0.0d) {
			throw new ApiException("MRP cannot be empty");
		}
		
		//insertCheck(p.getBrand(),p.getCategory());
		dao.insert(p,id);
	}

	@Transactional
	public void delete(int product_id) {
		dao.delete(product_id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public productDTO get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<productDTO> getAll() throws Exception {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, productPojo p) throws ApiException {
		normalize(p);   
		bar(p.getBarcode());
		dao.update(id,p);
	}

	@Transactional
	public productDTO getCheck(int id) throws ApiException {
		productDTO p = dao.select(id);
		if (p == null) {
			throw new ApiException("Product with given ID does not exit, id: " + id);
		}
		return p;
	}
	
	public brandPojo ref(int id) {
		return dao.findid(id);
	}
	
	public void bar(String barcode) throws ApiException{
		productDTO p =dao.selectbar(barcode);
		if(p!=null) {
			throw new ApiException("Barcode must be unique");
		}
	}
	
	
	

	protected static void normalize(productPojo p) {
		p.setName(StringUtil.toLowerCase(p.getName()));
		p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
	}
}
