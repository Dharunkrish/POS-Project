package com.increff.employee.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.inventoryDao;
import com.increff.employee.dao.productDao;
import com.increff.employee.model.Form.inventoryForm;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.util.DataConversionUtil;

@Service
public class InventoryService {

	@Autowired
	private inventoryDao dao;

	@Autowired
	private productDao pdao;
	private Logger logger = Logger.getLogger(inventoryDao.class);

	@Transactional(rollbackOn = ApiException.class)
	public void add(inventoryForm form) throws ApiException {
		inventoryPojo p = DataConversionUtil.convert(form);
		QuantityCheck(p);
		ProductCheck(p.getId());
		InvCheck(p.getId());
		dao.insert(p);
	}


	@Transactional(rollbackOn = ApiException.class)
	public inventoryForm get(int id) throws ApiException {
	    inventoryPojo i= getCheck(id);
        productPojo o=pdao.select(id);
        i.setBarcode(o.getBarcode());
		i.setName(o.getName());
		return DataConversionUtil.convert(dao.select(id));
	}

	@Transactional
	public List<inventoryForm> getAll() throws Exception {
	    List<inventoryPojo> i=dao.selectAll();
		List<inventoryForm> list2 = new ArrayList<inventoryForm>();
		for(inventoryPojo d:i){
          productPojo p=pdao.select(d.getId());
		  d.setName(p.getName());
		  d.setBarcode(p.getBarcode());
			list2.add(DataConversionUtil.convert(d));
		}
		return list2;
	}
	
	public List<productPojo> getid() throws Exception {
		return dao.selectid();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, inventoryForm f) throws ApiException {
		inventoryPojo p = DataConversionUtil.convert(f);
		inventoryPojo i=getCheck(id);
		i.setQuantity(p.getQuantity());
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
				throw new ApiException("Product with given barcode does not exit, id: "+ id);
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
