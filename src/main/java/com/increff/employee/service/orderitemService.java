package com.increff.employee.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
			dao.upd((int)p.getMrp(),p.getBarcode());
		    order.setName(p.getName());
			add(order);
		}
		return 1;
	}

	@Transactional(rollbackOn = ApiException.class)
	public int AddSingleItem(orderitemPojo o,int id) throws ApiException {
		productPojo p=checkitems(o,0);
		if (p.getProduct_id()==-1) {
			return 2;
		}	
		dao.upd((int) p.getMrp(),p.getBarcode());
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

	public productPojo checkprod(orderitemPojo o) throws ApiException {
        return dao.check(o.getBarcode());
        }
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(orderitemPojo o) throws ApiException {
		/*if (quantity==0) {
			dao.del_inv(p.getProduct_id());
		}*/
		dao.insert(o);
	}

	@Transactional
	public orderPojo create() {
		return dao.create();
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
		productPojo p=checkprod(o);
        if (p==null) {
        	return 0;
        }
        int q=check(o,p,quantity);
        if (q==-1) {
        	return 2;
        }
        o.setName(p.getName());
		dao.upd(q,o.getBarcode());
		dao.update(id,o);
		return 1;
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void delete(int id) throws ApiException {
		orderitemPojo oi=getid(id);
		productPojo p=dao.check(oi.getBarcode());
		inventoryPojo i=dao.prodquantity(p.getProduct_id());
		int quantity=i.getQuantity()+oi.getQuantity();
		dao.upd(quantity,oi.getBarcode());
		dao.delete(id);
	}
	
	protected static void normalize(orderitemPojo p) {
		p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
	}
}
