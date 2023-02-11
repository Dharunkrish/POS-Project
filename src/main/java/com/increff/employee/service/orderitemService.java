package com.increff.employee.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.inventoryDao;
import com.increff.employee.dao.orderitemDao;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.util.StringUtil;


@Service
public class orderitemService {

	@Autowired
	private orderitemDao dao;
	
	@Autowired
	private inventoryDao idao;
	
	private Logger logger = Logger.getLogger(orderitemDao.class);
	
	@Transactional(rollbackOn = ApiException.class)
	public productPojo checkitems(orderitemPojo o,int old_q) throws ApiException {
		normalize(o);
        productPojo p=checkprod(o);
        if (p==null) {
        	p=new productPojo();
        	p.setProduct_id(-1);
        	return p;
        }
		if (p.getMrp()<=o.getPrice()){
			throw new ApiException("Selling Price should not be greater than MRP");
		}
        int q=check(o,p,old_q);
        if (q==-1) {
        	p=new productPojo();
        	p.setProduct_id(-2);
        	return p;
        }
		p.setMrp(q);
		return p;
	}

	@Transactional(rollbackOn = ApiException.class)
	public int AddItems(List<orderitemPojo> item) throws ApiException {
		List<productPojo> inv_q=new ArrayList<productPojo>();
		for (orderitemPojo o:item){
		productPojo p=checkitems(o,0);
		    if (p.getProduct_id()==-1) {
		    	return 0;
		    }
			if (p.getProduct_id()==-2){
				return 2;
			}
			inv_q.add(p);
		}
		int id=create().getId();
		for(int i=0;i<item.size();i++) {
			orderitemPojo order=item.get(i);
			productPojo p=inv_q.get(i);
			order.setOrder_id(id);
			if (p.getMrp()==0){
				dao.del_inv(p.getProduct_id());
		}
		else{
		inventoryPojo q=idao.select(p.getProduct_id());
		q.setQuantity((int)p.getMrp());
		idao.update(q);
		}
		    order.setName(p.getName());
			add(order);
		}
		return 1;
	}

	@Transactional(rollbackOn = ApiException.class)
	public int AddSingleItem(orderitemPojo o,int id) throws ApiException {
		productPojo p=checkitems(o,0);
		if (p.getProduct_id()==-1) {
			return 0;
		}
		if (p.getProduct_id()==-2) {
			return 2;
		}	
        if (p.getMrp()==0){
				dao.del_inv(p.getProduct_id());
		}
		else{
		inventoryPojo i=idao.select(p.getProduct_id());
		i.setQuantity((int)p.getMrp());
		idao.update(i);
		}
	    o.setName(p.getName());
		o.setOrder_id(id);
		add(o);
		return 1;
	}

	@Transactional(rollbackOn = ApiException.class)
	public int check(orderitemPojo o,productPojo p,int old_q) throws ApiException {
            	 inventoryPojo i=dao.prodquantity(p.getProduct_id());
                 if ((o.getQuantity()-old_q)>i.getQuantity()) {
                	 return -1;
             }

             return i.getQuantity()-(o.getQuantity()-old_q);
	}

	@Transactional(rollbackOn = ApiException.class)
	public int editcheck(orderitemPojo o,productPojo p,int old_q) throws ApiException {
            	 inventoryPojo i=dao.prodquantity(p.getProduct_id());
				 int value=o.getQuantity()-old_q;
				 logger.info(i);
				 if (i==null){
                           if (value>0){
							return -1;
						   }
						   else{
							return Math.abs(o.getQuantity()-old_q);
						   }
				 }
                 else if ((o.getQuantity()-old_q)>i.getQuantity()) {
                	 return -1;
             }

             return i.getQuantity()-(o.getQuantity()-old_q);
	}



	public productPojo checkprod(orderitemPojo o) throws ApiException {
        return dao.check(o.getBarcode());
        }
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(orderitemPojo o) throws ApiException {
		dao.insert(o);
	}

	@Transactional
	public orderPojo create() {
		orderPojo o=new orderPojo();
		o.setT(ZonedDateTime.now(ZoneId.systemDefault()));
		o.setInvoiceGenerated(false);
		return dao.create(o);
	}
	
	@Transactional
	public List<orderPojo> getAll() throws Exception {
		return dao.selectAll();
	}
	
	@Transactional
	public orderPojo getorder(int id) throws Exception {
		return dao.selectid(id);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<orderitemPojo> get(int id) throws ApiException {
		return dao.selectitem(id);
	}
	
	public orderitemPojo getid(int id) throws ApiException {
		return dao.select(id);
	}
	
	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, orderPojo o) throws ApiException {
		dao.update(id,o);
	}

	@Transactional(rollbackOn  = ApiException.class)
	public int update(int id, orderitemPojo o, int quantity) throws ApiException {
		logger.info("j");
		productPojo p=checkprod(o);
        if (p==null) {
        	return 0;
        }
		if (p.getMrp()<=o.getPrice()){
			throw new ApiException("Selling Price should not be greater than MRP");
		}
        int q=editcheck(o,p,quantity);
        if (q==-1) {
        	return 2;
        }
		inventoryPojo i=idao.select(p.getProduct_id());
		logger.info("l"+i);
		if (i==null){
			if (q>0){
				inventoryPojo i1=new inventoryPojo();
				i1.setBarcode(p.getBarcode());
				i1.setId(p.getProduct_id());
				i1.setName(p.getName());
				i1.setQuantity(q);
				logger.info("l");
				idao.insert(i1);
			}
		}
		else if (q==0){
			dao.del_inv(p.getProduct_id());
	   }
	else{
	  i=idao.select(p.getProduct_id());
	i.setQuantity(q);
	idao.update(i);
	}
        o.setName(p.getName());
		dao.update(id,o);
		return 1;
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void delete(int id) throws ApiException {
		orderitemPojo oi=getid(id);
		productPojo p=dao.check(oi.getBarcode());
		inventoryPojo i=dao.prodquantity(p.getProduct_id());
		if (i==null){
			i=new inventoryPojo();
			i.setBarcode(oi.getBarcode());
			i.setId(p.getProduct_id());
			i.setName(p.getName());
			i.setQuantity(oi.getQuantity());
			idao.insert(i);
		}
		int quantity=i.getQuantity()+oi.getQuantity();
		dao.delete(id);
	}
	
	protected static void normalize(orderitemPojo p) {
		p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
	}
}
