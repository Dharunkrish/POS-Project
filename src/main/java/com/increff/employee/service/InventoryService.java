package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.inventoryDao;
import com.increff.employee.model.productDTO;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.productPojo;

@Service
public class InventoryService {

	@Autowired
	private inventoryDao dao;
	private Logger logger = Logger.getLogger(inventoryDao.class);

	@Transactional(rollbackOn = ApiException.class)
	public void add(inventoryPojo p) throws ApiException {
		productPojo pr=invcheck(p.getId());
		p.setBarcode(pr.getBarcode());
		p.setName(pr.getName());
		dao.insert(p);
	}


	@Transactional(rollbackOn = ApiException.class)
	public inventoryPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<inventoryPojo> getAll() throws Exception {
		return dao.selectAll();
	}
	
	public List<productPojo> getid() throws Exception {
		return dao.selectid();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, inventoryPojo p) throws ApiException {
		//invcheck(p.getId());
		dao.update(id,p);
	}

	@Transactional
	public inventoryPojo getCheck(int id) throws ApiException {
		inventoryPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("inventory with given ID does not exit, id: " + id);
		}
		return p;
	}
	
	@Transactional
	public productPojo invcheck(int id) throws ApiException {
		productPojo pr=dao.findid(id);
		logger.info(pr);
		  if (pr==null) {
				logger.info("delete_id");
				throw new ApiException("Product with given Product ID does not exit, id: "+ id);
			}	
		inventoryPojo i=dao.select(id);
		if (i!=null) {
			throw new ApiException("Inventory details for the given Product ID already exists");
		}
		return pr;
	}
	
	
	

}
