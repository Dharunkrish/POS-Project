package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.inventoryDao;
import com.increff.employee.dao.productDao;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.productPojo;

@Service
public class InventoryService {

	@Autowired
	private inventoryDao dao;

	@Autowired
	private productDao pdao;
	private Logger logger = Logger.getLogger(inventoryDao.class);

	@Transactional(rollbackOn = ApiException.class)
	public void add(inventoryPojo p) throws ApiException {
		QuantityCheck(p);
		ProductCheck(p.getId());
		InvCheck(p.getId());
		dao.insert(p);
	}


	@Transactional(rollbackOn = ApiException.class)
	public inventoryPojo get(int id) throws ApiException {
	    inventoryPojo i= getCheck(id);
        productPojo o=pdao.select(id);
        i.setBarcode(o.getBarcode());
		i.setName(o.getName());
		return i;
	}

	@Transactional
	public List<inventoryPojo> getAll() throws Exception {
	    List<inventoryPojo> i=dao.selectAll();
		for(inventoryPojo d:i){
          productPojo p=pdao.select(d.getId());
		  d.setName(p.getName());
		  d.setBarcode(p.getBarcode());
		}
		return i;
	}
	
	public List<productPojo> getid() throws Exception {
		return dao.selectid();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, inventoryPojo p) throws ApiException {
		inventoryPojo i=getCheck(id);
		i.setQuantity(p.getQuantity());
		logger.info(i.getId()+""+i.getBarcode()+i.getName()+i.getQuantity());
		dao.update(i);
	}

	@Transactional
	public inventoryPojo getCheck(int id) throws ApiException {
		inventoryPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("inventory with given ID does not exit, id: " + id);
		}
		productPojo p1=pdao.select(p.getId());
        p.setBarcode(p1.getBarcode());
		p.setName(p1.getName());
		return p;
	}
	@Transactional
	public void QuantityCheck(inventoryPojo i) throws ApiException {
		if (i.getQuantity()<=0){
			throw new ApiException("Quantity should not be zero or negative");
		}
	}

	@Transactional
	public productPojo ProductCheck(int id) throws ApiException {
		productPojo pr=dao.findid(id);
		  if (pr==null) {
				throw new ApiException("Product with given Product ID does not exit, id: "+ id);
			}
			return pr;
		}	
	@Transactional
	public void InvCheck(int id) throws ApiException {
		inventoryPojo ip=dao.select(id);
		if (ip!=null) {
			throw new ApiException("Inventory details for the given Product ID already exists");
		}
	}
}
