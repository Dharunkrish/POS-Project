package com.increff.employee.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.controller.productApiController;
import com.increff.employee.dao.brandDao;
import com.increff.employee.model.Data.brandData;
import com.increff.employee.model.Form.brandForm;
import com.increff.employee.pojo.brandPojo;
import com.increff.employee.util.DataConversionUtil;
import com.increff.employee.util.StringUtil;

@Service
public class brandService {

	@Autowired
	private brandDao dao;
	private Logger logger = Logger.getLogger(productApiController.class);

	@Transactional(rollbackOn = ApiException.class)
	public void add(brandForm f) throws ApiException {
		brandPojo p = DataConversionUtil.convert(f);
		normalize(p);
		insertCheck(p.getBrand(),p.getCategory());
		dao.insert(p);
	}


	@Transactional(rollbackOn = ApiException.class)
	public brandData get(int id) throws ApiException {
		return 	DataConversionUtil.convert(getCheck(id));
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<brandData> getbrand(brandForm form) throws ApiException {
		brandPojo b = DataConversionUtil.convert(form);
		List<brandPojo> br=new ArrayList<brandPojo>();
		if ((b.getBrand()==null || b.getBrand()=="") && (b.getCategory()==null || b.getCategory()=="")) {
			br= dao.selectAll();
		}
		else if ((b.getBrand()==null || b.getBrand()=="")) {
			br= dao.getcategory(b);
		}
		else if((b.getCategory()==null || b.getCategory()=="")) {
			br= dao.getbrand(b);

		}
		List<brandData> list2 = new ArrayList<brandData>();
		for (brandPojo bd : br) {
			list2.add(DataConversionUtil.convert(bd));
		}
		return list2;
	}
	


	@Transactional
	public List<brandData> getAll() {
		List<brandPojo> list= dao.selectAll();
		List<brandData> list2 = new ArrayList<brandData>();
		for (brandPojo p : list) {
			list2.add(DataConversionUtil.convert(p));
		}
		return list2;
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, brandForm f) throws ApiException {
		brandPojo p = DataConversionUtil.convert(f);
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
