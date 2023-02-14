package com.increff.employee.util;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.increff.employee.model.DaySalesXmlForm;
import com.increff.employee.model.InventoryXmlForm;
import com.increff.employee.model.SalesReportDataXml;
import com.increff.employee.model.UserData;
import com.increff.employee.model.UserForm;
import com.increff.employee.model.booForm;
import com.increff.employee.model.brandData;
import com.increff.employee.model.brandForm;
import com.increff.employee.model.daySalesReportForm;
import com.increff.employee.model.inventoryForm;
import com.increff.employee.model.orderData;
import com.increff.employee.model.orderForm;
import com.increff.employee.model.orderitemForm;
import com.increff.employee.model.reportData;
import com.increff.employee.pojo.UserPojo;
import com.increff.employee.pojo.brandPojo;
import com.increff.employee.pojo.daySalesReportPojo;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.pojo.orderitemPojo;


public class DataConversionUtil {

	public static Logger logger = Logger.getLogger(pdfconversionUtil.class);
	
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static UserData convert(UserPojo p) {
		UserData d = new UserData();
		d.setEmail(p.getEmail());
		d.setRole(p.getRole());
		d.setId(p.getId());
		return d;
	}

	public static UserPojo convert(UserForm f) {
		UserPojo p = new UserPojo();
		p.setEmail(f.getEmail());
		p.setRole(f.getRole());
		p.setPassword(f.getPassword());
		return p;
	}
	
	public static brandData convert(brandPojo p) {
		brandData d = new brandData();
		d.setBrand(p.getBrand());
		d.setCategory(p.getCategory());
		d.setId(p.getId());
		return d;
	}

	public static brandPojo convert(brandForm f) {
		brandPojo p = new brandPojo();
		p.setBrand(f.getBrand());
		p.setCategory(f.getCategory());
		return p;
	}
	
	public static inventoryForm convert(inventoryPojo p) {
		inventoryForm d = new inventoryForm();
		d.setQuantity(p.getQuantity());
		d.setId(p.getId());
		d.setBarcode(p.getBarcode());
		d.setName(p.getName());
		return d;
	}

	public static inventoryPojo convert(inventoryForm f) {
		inventoryPojo pr = new inventoryPojo();
		pr.setQuantity(f.getQuantity());
		pr.setId(f.getId());
		return pr;
	}
	
	 public static orderData convert(orderitemPojo orderitem) {
			orderData d=new orderData();
			d.setName(orderitem.getName());
			d.setQuantity(orderitem.getQuantity());
			d.setMrp(orderitem.getPrice());
			d.setPrice(orderitem.getQuantity()*orderitem.getPrice());
			return d;
		 }

		 public static List<DaySalesXmlForm> convert(List<daySalesReportPojo> ds) {
			List<DaySalesXmlForm> s=new ArrayList<DaySalesXmlForm>();
			for(daySalesReportPojo d:ds){
			DaySalesXmlForm r=new  DaySalesXmlForm();
			r.setDate(formatter.format(d.getDate()));
			r.setTotal_order(d.getTotal_orders());
			r.setTotal_item(d.getTotal_items());
			r.setRevenue(d.getRevenue());
			s.add(r);
			}
			return s;
		}
		 
			public static daySalesReportForm convert2(List<Object> p) {
				daySalesReportForm r=new daySalesReportForm();
				r.setCount((int) p.get(0));
				r.setRevenue((double) p.get(1));
				r.setBrand((String) p.get(2));
				r.setCategory((String) p.get(3));
				return r;
			}
		 
		public static List<SalesReportDataXml> convert1(Map<Integer,List<Object>> m) {
			List<SalesReportDataXml> s=new ArrayList<SalesReportDataXml>();
			for (int b:m.keySet()) {
				List<Object> p=m.get(b);
				SalesReportDataXml r=new SalesReportDataXml();
				r.setQuantity((int) p.get(0));
				r.setRevenue((double) p.get(1));
				r.setBrand((String) p.get(2));
				r.setCategory((String) p.get(3));
				s.add(r);
			 }
				return s;
			}

		public static List<InventoryXmlForm> convert2(Map<Integer,List<Object>> m) {
			List<InventoryXmlForm> s=new ArrayList<InventoryXmlForm>();
			for (int b:m.keySet()) {
				List<Object> p=m.get(b);
				InventoryXmlForm r=new InventoryXmlForm();
				r.setQuantity((int) p.get(0));
				r.setBrand((String) p.get(1));
				r.setCategory((String) p.get(2));
				s.add(r);
			 }
				return s;
			}
		
		public static orderForm convert(orderPojo p) {
			orderForm d = new orderForm();
			d.setId(p.getId());
			d.setTime(p.getT().format(formatter));
			d.setInvoiceGenerated(p.isInvoiceGenerated());
			return d;
		}

		public static orderitemPojo convert(orderitemForm f) {
			orderitemPojo o = new orderitemPojo();
			o.setQuantity(f.getQuantity());
			o.setBarcode(f.getBarcode());
			o.setPrice(f.getPrice());
			o.setName(f.getName());
			return o;
		}
		
		
		
		public static booForm convert(int is_p) {
			booForm form=new booForm();
			form.setIs_p(is_p);
			return form;
		}

		public static booForm convert(int is_p,String name) {
			booForm form=new booForm();
			form.setIs_p(is_p);
			form.setName(name);
			return form;
		}
		
		public static reportData convert(daySalesReportPojo d) {
			reportData r=new reportData();
			r.setDate(formatter.format(d.getDate()));
			r.setTotal_order(d.getTotal_orders());
			r.setTotal_item(d.getTotal_items());
			r.setRevenue(d.getRevenue());
			return r;
		}
		

		
		public static daySalesReportForm convert1(List<Object> p) {
			daySalesReportForm r=new daySalesReportForm();
			r.setCount((int) p.get(0));
			r.setBrand((String) p.get(1));
			r.setCategory((String) p.get(2));
			return r;
		}
		

}


