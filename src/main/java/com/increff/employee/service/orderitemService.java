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
import com.increff.employee.model.Form.OrderItemData;
import com.increff.employee.model.Form.orderForm;
import com.increff.employee.model.Form.orderitemForm;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.util.DataConversionUtil;
import com.increff.employee.util.StringUtil;


@Service
public class orderitemService {

	@Autowired
	private orderitemDao dao;
	
	@Autowired
	private inventoryDao idao;
	
	private Logger logger = Logger.getLogger(orderitemDao.class);
	
	@Transactional(rollbackOn = ApiException.class)
	public List<Object> checkitems(orderitemPojo o,int old_q) throws ApiException {
		List<Object> res=new ArrayList<Object>();
		normalize(o);
        productPojo p=checkprod(o);
        if (p==null) {
        	throw new ApiException("Product with given barcode does not exist");
        }
		if (p.getMrp()<=o.getPrice()){
			throw new ApiException("Selling Price should not be greater than MRP");
		}
        List<Integer> q=check(o,p,old_q);
        if (q.get(0)==-2) {
			throw new ApiException("Present inventory availability for "+p.getName()+" is only "+q.get(1)+" items");
        }
		res.add(p.getProduct_id());
		res.add(q.get(1));
		res.add(p.getName());
		return res;
	}

	@Transactional(rollbackOn = ApiException.class)
	public List<Integer> AddItems(List<orderitemForm> form) throws ApiException {
		List <orderitemPojo> item=new ArrayList<orderitemPojo>();
		for(orderitemForm f:form) {
			item.add(DataConversionUtil.convert(f));
		}
		List<List<Object>> inv_q=new ArrayList<List<Object>>();
		List<Integer> res=new ArrayList<Integer>();
		for (orderitemPojo o:item){
		List<Object> p=checkitems(o,0);
			inv_q.add(p);
		}
		int id=create().getId();
		for(int i=0;i<item.size();i++) {
			orderitemPojo order=item.get(i);
			List<Object> p=inv_q.get(i);
			order.setOrder_id(id);
			if ((int)p.get(1)==0){
				dao.del_inv((int)p.get(0));
		}
		else{
		inventoryPojo q=idao.select((int)p.get(0));
		q.setQuantity((int)p.get(1));
		idao.update(q);
		}
		    order.setName((String)p.get(2));
			add(order);
		}
		res.add(1);
		res.add(2);
		return res;
	}

	@Transactional(rollbackOn = ApiException.class)
	public void AddSingleItem(orderitemForm form,int id) throws ApiException {
		orderitemPojo o=DataConversionUtil.convert(form);
		List<Object> p=checkitems(o,0);
        if ((int)p.get(1)==0){
				dao.del_inv((int)p.get(0));
		}
		else{
		inventoryPojo i=idao.select((int)p.get(0));
		i.setQuantity((int)p.get(1));
		idao.update(i);
		}
	    o.setName((String)p.get(2));
		o.setOrder_id(id);
		add(o);
	}

	@Transactional(rollbackOn = ApiException.class)
	public List<Integer> check(orderitemPojo o,productPojo p,int old_q) throws ApiException {
            	 inventoryPojo i=dao.prodquantity(p.getProduct_id());
				 List<Integer> res=new ArrayList<Integer>();
				 if (i==null){
					res.add(-2);
					res.add(0);
					return res;
				 }
                 if ((o.getQuantity()-old_q)>i.getQuantity()) {
					res.add(-2);
					res.add(i.getQuantity());
                	 return res;
             }
            res.add(1);
            res.add(i.getQuantity()-(o.getQuantity()-old_q));
			return res;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public int editcheck(orderitemPojo o,productPojo p,int old_q) throws ApiException {
            	 inventoryPojo i=dao.prodquantity(p.getProduct_id());
				 int value=o.getQuantity()-old_q;
				 if (i==null){
                           if (value>0){
							throw new ApiException("Present inventory availability is only 0 items");
						   }
						   else{
							return Math.abs(o.getQuantity()-old_q);
						   }
				 }
                 else if ((o.getQuantity()-old_q)>i.getQuantity()) {

					throw new ApiException("Present inventory availability is only "+(i.getQuantity()+old_q)+" items");

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
	public List<orderForm> getAll() throws Exception {
		List<orderForm> list2=new ArrayList<orderForm>();
		for(orderPojo p:dao.selectAll()){
			list2.add(DataConversionUtil.convert(p));
		}
		return list2;
	}
	
	public OrderItemData checkitem(orderitemForm form) throws ApiException{
		List<Object> p=checkitems(DataConversionUtil.convert(form),0);
        if ((int)p.get(0)==-1) {
        	return DataConversionUtil.convert(0,"",0);
        }
        else if ((int)p.get(0)==-2) {
        	return DataConversionUtil.convert(2,"",(int)p.get(1));
        }
        return DataConversionUtil.convert(1,(String)p.get(2),0);
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
		orderPojo oo=dao.selectid(id);
		oo.setInvoiceGenerated(true);
		dao.update(oo);
	}

	@Transactional(rollbackOn  = ApiException.class)
	public int update(int id, orderitemForm f, int quantity) throws ApiException {
		int order_id=dao.selectitemid(id).getOrder_id();
		orderPojo od=dao.selectid(order_id);
		if (od.isInvoiceGenerated()==true) {
			throw new ApiException("This order cannot be edited as invoice is already generated");
		}
        orderitemPojo o=DataConversionUtil.convert(f);    
		productPojo p=checkprod(o);
		double mrp=checkprod(o).getMrp();
        if (p==null) {
			throw new ApiException("Product with given barcode does not exist");
        }
        System.err.print(mrp+""+o.getPrice());
		if (mrp<=o.getPrice()){
			throw new ApiException("Selling Price should not be greater than MRP");
		}
        int q=editcheck(o,p,quantity);
		inventoryPojo i=idao.select(p.getProduct_id());
		System.err.print(p.getProduct_id());
		if (i==null){
			if (q>0){
				inventoryPojo i1=new inventoryPojo();
				i1.setBarcode(p.getBarcode());
				i1.setId(p.getProduct_id());
				i1.setName(p.getName());
				i1.setQuantity(q);
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
	orderitemPojo o1=dao.selectitemid(id);
	o1.setBarcode(p.getBarcode());
	o1.setName(p.getName());
	o1.setPrice(o.getPrice());
	o1.setQuantity(o.getQuantity());
	dao.update(o1);
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
			System.err.print(i.getId());
			idao.insert(i);
		}
		else{
			i.setQuantity(i.getQuantity()+oi.getQuantity());
			idao.update(i);
		}
		dao.delete(id);
	}
	
	protected static void normalize(orderitemPojo p) {
		p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
	}
}
