package com.increff.employee.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.increff.employee.model.Data.UserData;
import com.increff.employee.model.Data.brandData;
import com.increff.employee.model.Data.productData;
import com.increff.employee.model.Data.reportData;
import com.increff.employee.model.Form.UserForm;
import com.increff.employee.model.Form.OrderItemData;
import com.increff.employee.model.Form.brandForm;
import com.increff.employee.model.Form.daySalesReportForm;
import com.increff.employee.model.Form.inventoryForm;
import com.increff.employee.model.Form.orderForm;
import com.increff.employee.model.Form.orderitemForm;
import com.increff.employee.model.Form.productForm;
import com.increff.employee.model.Xml.DaySalesXmlForm;
import com.increff.employee.model.Xml.InventoryXmlForm;
import com.increff.employee.model.Xml.SalesReportDataXml;
import com.increff.employee.model.Xml.orderData;
import com.increff.employee.pojo.UserPojo;
import com.increff.employee.pojo.brandPojo;
import com.increff.employee.pojo.daySalesReportPojo;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.pojo.orderitemPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.service.ApiException;


public class DataConversionUtil {

	public static Logger logger = Logger.getLogger(pdfconversionUtil.class);
	
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DecimalFormat df = new DecimalFormat("0.00");


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
		 
		public static List<SalesReportDataXml> convert4(List<daySalesReportForm> m) {
			List<SalesReportDataXml> s=new ArrayList<SalesReportDataXml>();
			for (daySalesReportForm b:m) {
				SalesReportDataXml r=new SalesReportDataXml();
				r.setQuantity(b.getCount());
				r.setBrand(b.getBrand());
				r.setCategory(b.getCategory());
				r.setRevenue(b.getRevenue());
				s.add(r);
			 }
				return s;
			}

		public static List<InventoryXmlForm> convert3(List<daySalesReportForm> m) {
			List<InventoryXmlForm> s=new ArrayList<InventoryXmlForm>();
			for (daySalesReportForm b:m) {
				InventoryXmlForm r=new InventoryXmlForm();
				r.setQuantity(b.getCount());
				r.setBrand(b.getBrand());
				r.setCategory(b.getCategory());
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
			BigDecimal bd = new BigDecimal(f.getPrice()).setScale(2, RoundingMode.HALF_UP); 
			o.setPrice(bd.doubleValue());
			logger.info(o.getPrice());
			o.setName(f.getName());
			return o;
		}
		
		
		
		public static OrderItemData convert(int is_p) {
			OrderItemData form=new OrderItemData();
			form.setIs_p(is_p);
			return form;
		}

		public static OrderItemData convert(int is_p,int quantity) {
			OrderItemData form=new OrderItemData();
			form.setIs_p(is_p);
			form.setQuantity(quantity);
			return form;
		}

		public static OrderItemData convert(int is_p,String name,int quantity) {
			OrderItemData form=new OrderItemData();
			form.setIs_p(is_p);
			form.setName(name);
			form.setQuantity(quantity);
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
		
		public static productData convert(productPojo p) {
			productData d = new productData();
			d.setBrand_Category_id(p.getBrand_Category_id());
			d.setName(p.getName());
			d.setBarcode(p.getBarcode());
			d.setMrp(p.getMrp());
			d.setProduct_id(p.getProduct_id());
			return d;
		}

		public static productPojo convert(productForm f) throws ApiException {
			productPojo p=new productPojo();
			p.setName(f.getName());
			p.setBarcode(f.getBarcode());
			p.setMrp(f.getMrp());
			p.setBrand_Category_id(f.getBrand_Category_id());
			return p;
		}
		

}


