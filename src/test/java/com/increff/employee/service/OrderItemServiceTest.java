package com.increff.employee.service;

import static org.junit.Assert.assertEquals;
import org.junit.rules.ExpectedException;
import org.junit.Rule;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;



import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.dao.inventoryDao;
import com.increff.employee.dao.orderitemDao;
import com.increff.employee.pojo.brandPojo;
import com.increff.employee.pojo.inventoryPojo;
import com.increff.employee.pojo.productPojo;
import com.increff.employee.pojo.orderPojo;
import com.increff.employee.pojo.orderitemPojo;




public class OrderItemServiceTest extends AbstractUnitTest {

	@Autowired
	private brandService brandservice;

	@Autowired
	private orderitemService service;

	@Autowired
	private InventoryService invservice;

	@Autowired
	private productService prodservice;

	@Autowired
	private orderitemDao idao;

	@Autowired
	private inventoryDao indao;


	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
    
	public brandPojo BrandInitialise1()  throws ApiException {
		brandPojo p = new brandPojo();
        p.setBrand("nestle");
        p.setCategory("maggi");
		brandservice.add(p);
		return p;
	}

	public brandPojo BrandInitialise2()  throws ApiException {
		brandPojo p = new brandPojo();
        p.setBrand("bru1");
        p.setCategory("coffee");
		brandservice.add(p);
		return p;
	}

	//Initialise Product 1
	public productPojo prodInitialise1()  throws ApiException {
		brandPojo b=BrandInitialise1();
		productPojo p = new productPojo();
        p.setName("150 g");
		p.setBarcode("1");
		p.setMrp(1000.0);
		p.setBrand_Category_id(b.getId());
		prodservice.add(p);
		return p;
	}

	//Initialise Product 2
	public productPojo prodInitialise2()  throws ApiException {
		brandPojo b=BrandInitialise2();
		productPojo p = new productPojo();
        p.setName("1 liter");
		p.setBarcode("2");
		p.setMrp(2000.0);
		p.setBrand_Category_id(b.getId());
		prodservice.add(p);
		return p;
	}

	public inventoryPojo InvInitialise1()  throws ApiException {
		productPojo p=prodInitialise1();
		inventoryPojo i = new inventoryPojo();
		i.setId(p.getProduct_id());
        i.setName("150 g");
		i.setBarcode("1");
		i.setQuantity(10);
		invservice.add(i);
		return i;
	}

	public inventoryPojo InvInitialise2()  throws ApiException {
	    productPojo p=prodInitialise2();
		inventoryPojo i = new inventoryPojo();
        i.setId(p.getProduct_id());
        i.setName("1 liter");
		i.setBarcode("2");
		i.setQuantity(20);
		invservice.add(i);
		return i;
	}

	public orderitemPojo Initialise2()  throws ApiException {
		inventoryPojo i = new inventoryPojo();
		orderitemPojo oi=new orderitemPojo();
        oi.setName("1 Liter");
		oi.setBarcode("2");
		oi.setQuantity(20);
		oi.setPrice(100);
		invservice.add(i);
		return oi;
	}
    public orderPojo Initialise1() throws Exception{
	List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		inventoryPojo i1=InvInitialise1();
		inventoryPojo i2=InvInitialise2();
		orderitemPojo oi1=new orderitemPojo();
		orderitemPojo oi2=new orderitemPojo();
		oi1.setBarcode(i1.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i1.getQuantity()-1);
		item.add(oi1);
		oi2.setBarcode(i2.getBarcode());
		oi2.setPrice(2);
		oi2.setQuantity(i2.getQuantity()-2);
		item.add(oi2);

		//order add
		service.AddItems(item);
		return service.getAll().get(0);
	}

	@Test
	public void TestOrderCreate() throws Exception{
		   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
           orderPojo o=service.create();
		   assertEquals(service.getAll().get(0).getId(), o.getId());
		   assertEquals(false, o.isInvoiceGenerated());
		   assertEquals( ZonedDateTime.now().format(formatter),o.getT().format(formatter));
	}

	@Test
	public void TestNormalize(){
	orderitemPojo oi=new orderitemPojo();
	oi.setBarcode(" S d ");
	orderitemService.normalize(oi);
	assertEquals(oi.getBarcode(), "s d");
	}

	@Test
	public void TestCheckProduct() throws ApiException{
	orderitemPojo oi=new orderitemPojo();
	oi.setBarcode(" Sd ");
	oi.setPrice(1);
	oi.setQuantity(10);
    productPojo p=service.checkitems(oi,0);
	assertEquals(p.getProduct_id(), -1);
	}

	@Test
	public void TestCheckInventory() throws ApiException{
	inventoryPojo i=InvInitialise1();
	orderitemPojo oi=new orderitemPojo();
	oi.setBarcode(i.getBarcode());
	oi.setPrice(1);
	oi.setQuantity(i.getQuantity()+1);
    productPojo p=service.checkitems(oi,0);
	assertEquals(p.getProduct_id(), -2);
	}

	@Test
	public void TestCheckItems() throws ApiException{
	inventoryPojo i=InvInitialise1();
	orderitemPojo oi=new orderitemPojo();
	oi.setBarcode(i.getBarcode());
	oi.setPrice(1);
	oi.setQuantity(i.getQuantity());
    productPojo p=service.checkitems(oi,0);
	assertEquals(p.getProduct_id(), i.getId());
	assertEquals(p.getName(), i.getName());
	}

	@Test
	public void TestCheckItemsSellingPrice() throws ApiException{
	inventoryPojo i=InvInitialise1();
	orderitemPojo oi=new orderitemPojo();
	oi.setBarcode(i.getBarcode());
	oi.setPrice(1002);
	oi.setQuantity(i.getQuantity());
	exceptionRule.expect(ApiException.class);
	exceptionRule.expectMessage("Selling Price should not be greater than MRP");
    service.checkitems(oi,0);
	}

	@Test
	public void TestAddOrderItem() throws Exception{
		//Initialise
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		inventoryPojo i1=InvInitialise1();
		inventoryPojo i2=InvInitialise2();
		orderitemPojo oi1=new orderitemPojo();
		orderitemPojo oi2=new orderitemPojo();
		oi1.setBarcode(i1.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i1.getQuantity()-1);
		item.add(oi1);
		oi2.setBarcode(i2.getBarcode());
		oi2.setPrice(2);
		oi2.setQuantity(i2.getQuantity()-2);
		item.add(oi2);

		//order add
		service.AddItems(item);
		
		//checking added order
		List<orderPojo> order=service.getAll();
		assertEquals(order.size(), 1);
		int order_id=order.get(0).getId();
		List<orderitemPojo> items=service.get(order_id);
		orderitemPojo item1=items.get(0);
		assertEquals(item1.getBarcode(), i1.getBarcode());
		assertEquals(item1.getOrder_id(), order_id);
		assertEquals(item1.getName(), i1.getName());
		assertEquals(item1.getPrice(), oi1.getPrice(),0.01);
		assertEquals(item1.getQuantity(), oi1.getQuantity());
		orderitemPojo item2=items.get(1);
		assertEquals(item2.getBarcode(), i2.getBarcode());
		assertEquals(item2.getOrder_id(), order_id);
		assertEquals(item2.getName(), i2.getName());
		assertEquals(item2.getPrice(), oi2.getPrice(),0.01);
		assertEquals(item2.getQuantity(), oi2.getQuantity());
        assertEquals(indao.select(indao.selectAll().get(0).getId()).getQuantity(), 1);
		assertEquals(indao.select(indao.selectAll().get(1).getId()).getQuantity(), 2);
	}

	@Test
	public void TestAddOrderItemInvDel() throws Exception{
		//Initialise
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		inventoryPojo i1=InvInitialise1();
		inventoryPojo i2=InvInitialise2();
		orderitemPojo oi1=new orderitemPojo();
		orderitemPojo oi2=new orderitemPojo();
		int inv_id1=(indao.selectAll().get(0).getId());
		oi1.setBarcode(i1.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i1.getQuantity());
		item.add(oi1);
		oi2.setBarcode(i2.getBarcode());
		oi2.setPrice(2);
		oi2.setQuantity(i2.getQuantity()-2);
		item.add(oi2);

		//order add
		service.AddItems(item);

		//checking added order
        assertEquals(indao.select(inv_id1), null);
	}


	@Test
	public void TestAddWrongOrderItem1() throws Exception{
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		inventoryPojo i1=InvInitialise1();
		orderitemPojo oi1=new orderitemPojo();
		oi1.setBarcode(i1.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i1.getQuantity()+2);
		item.add(oi1);
		assertEquals(2,service.AddItems(item));
        assertEquals(0, service.getAll().size());

	}

	@Test
	public void TestAddWrongOrderItem2() throws Exception{
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		inventoryPojo i1=InvInitialise1();
		orderitemPojo oi1=new orderitemPojo();
		oi1.setBarcode("sdf");
		oi1.setPrice(1);
		oi1.setQuantity(i1.getQuantity()+2);
		item.add(oi1);
		assertEquals(0,service.AddItems(item));
		assertEquals(0, service.getAll().size());
	}

	@Test
	public void TestAddSingleOrderItem() throws Exception{
		inventoryPojo i=InvInitialise1();
		orderitemPojo oi=new orderitemPojo();
		oi.setBarcode(i.getBarcode());
		oi.setPrice(1);
		oi.setQuantity(i.getQuantity()-5);
		orderPojo o=service.create();
		service.AddSingleItem(oi, o.getId());
		orderitemPojo item=service.get(o.getId()).get(0);
		assertEquals(item.getBarcode(), i.getBarcode());
		assertEquals(item.getOrder_id(), o.getId());
		assertEquals(item.getName(), i.getName());
		assertEquals(item.getPrice(), oi.getPrice(),0.01);
		assertEquals(item.getQuantity(), oi.getQuantity());
	}

	@Test
	public void TestAddSingleWrongOrderItem1() throws Exception{
		inventoryPojo i=InvInitialise1();
		orderitemPojo oi=new orderitemPojo();
		oi.setBarcode(i.getBarcode());
		oi.setPrice(1);
		oi.setQuantity(i.getQuantity()+5);
		orderPojo o=service.create();
		assertEquals(2,service.AddSingleItem(oi, o.getId()));
	}

	@Test
	public void TestAddSingleWrongOrderItem2() throws Exception{
		inventoryPojo i=InvInitialise1();
		orderitemPojo oi=new orderitemPojo();
		oi.setBarcode("dffds");
		oi.setPrice(1);
		oi.setQuantity(i.getQuantity()+5);
		orderPojo o=service.create();
		assertEquals(0,service.AddSingleItem(oi, o.getId()));
	}

	@Test
	public void TestOrderGetAll() throws Exception{
		orderPojo o1=service.create();
		orderPojo o2=service.create();
        List<orderPojo> o=service.getAll();
		assertEquals(o.size(), 2);
		assertEquals(o1.getId(), o.get(0).getId());
		assertEquals(o2.getId(), o.get(1).getId());
	}

	@Test
	public void TestGetItem() throws Exception{
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		inventoryPojo i1=InvInitialise1();
		inventoryPojo i2=InvInitialise2();
		orderitemPojo oi1=new orderitemPojo();
		orderitemPojo oi2=new orderitemPojo();
		oi1.setBarcode(i1.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i1.getQuantity());
		item.add(oi1);
		oi2.setBarcode(i2.getBarcode());
		oi2.setPrice(2);
		oi2.setQuantity(i2.getQuantity());
		item.add(oi2);
		service.AddItems(item);
		int order_id=service.getAll().get(0).getId();
		List<orderitemPojo> items=service.get(order_id);
		orderitemPojo item1=items.get(0);
		assertEquals(item1.getBarcode(), i1.getBarcode());
		assertEquals(item1.getOrder_id(), order_id);
		assertEquals(item1.getName(), i1.getName());
		assertEquals(item1.getPrice(), oi1.getPrice(),0.01);
		assertEquals(item1.getQuantity(), oi1.getQuantity());
		orderitemPojo item2=items.get(1);
		assertEquals(item2.getBarcode(), i2.getBarcode());
		assertEquals(item2.getOrder_id(), order_id);
		assertEquals(item2.getName(), i2.getName());
		assertEquals(item2.getPrice(), oi2.getPrice(),0.01);
		assertEquals(item2.getQuantity(), oi2.getQuantity());
	}

	@Test
	public void TestGetOrderId() throws Exception{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		orderPojo o=service.create();
		orderPojo order=service.getorder(o.getId());
		assertEquals(order.getId(), o.getId());
		assertEquals(order.getT().format(formatter), o.getT().format(formatter));
        assertEquals(order.isInvoiceGenerated(), o.isInvoiceGenerated());
	}

	@Test
	public void TestGetOrderitemId() throws Exception{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		orderPojo o=service.create();
		orderPojo order=service.getorder(o.getId());
		assertEquals(order.getId(), o.getId());
		assertEquals(order.getT().format(formatter), o.getT().format(formatter));
        assertEquals(order.isInvoiceGenerated(), o.isInvoiceGenerated());
	}

	@Test
	public void TestGetItemId() throws ApiException{
		inventoryPojo i1=InvInitialise1();
		orderitemPojo oi1=new orderitemPojo();
		oi1.setBarcode(i1.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i1.getQuantity());
		orderPojo o=service.create();
		service.AddSingleItem(oi1, o.getId());
		int id=service.get(o.getId()).get(0).getId();
		orderitemPojo item1=service.getid(id);
		assertEquals(item1.getBarcode(), i1.getBarcode());
		assertEquals(item1.getOrder_id(), o.getId());
		assertEquals(item1.getName(), i1.getName());
		assertEquals(item1.getPrice(), oi1.getPrice(),0.01);
		assertEquals(item1.getQuantity(), oi1.getQuantity());
	}

	@Test
	public void TestEditCheckNoInvLessInv() throws Exception{
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		inventoryPojo i2=InvInitialise2();
		orderitemPojo oi1=new orderitemPojo();
		oi1.setBarcode(i2.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i2.getQuantity());
		item.add(oi1);
		service.AddItems(item);
		int order_id=service.getAll().get(0).getId();
		orderitemPojo oi2=service.get(order_id).get(0);
		int quantity=oi2.getQuantity();
		oi2.setPrice(1);
		oi2.setQuantity(i2.getQuantity()+1);
		productPojo p=new productPojo();
		p.setProduct_id(i2.getId());
        assertEquals(-1,service.editcheck(oi2,p,i2.getQuantity()));	
	}

	@Test
	public void TestEditCheckNoInv() throws Exception{
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		inventoryPojo i2=InvInitialise2();
		orderitemPojo oi1=new orderitemPojo();
		oi1.setBarcode(i2.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i2.getQuantity());
		item.add(oi1);
		service.AddItems(item);
		int order_id=service.getAll().get(0).getId();
		orderitemPojo oi2=service.get(order_id).get(0);
		oi2.getQuantity();
		oi2.setPrice(1);
		oi2.setQuantity(i2.getQuantity()-1);
		productPojo p=new productPojo();
		p.setProduct_id(i2.getId());
        assertEquals(1,service.editcheck(oi2,p,i2.getQuantity()));
	}

	@Test
	public void TestEditCheckLessInv() throws Exception{
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		inventoryPojo i2=InvInitialise2();
		orderitemPojo oi1=new orderitemPojo();
		oi1.setBarcode(i2.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i2.getQuantity()-1);
		item.add(oi1);
		service.AddItems(item);
		int order_id=service.getAll().get(0).getId();
		orderitemPojo oi2=service.get(order_id).get(0);
		int quantity=oi2.getQuantity();
		oi2.setPrice(1);
		oi2.setQuantity(quantity+2);
		productPojo p=new productPojo();
		p.setProduct_id(i2.getId());
        assertEquals(-1,service.editcheck(oi2,p,quantity));
	}

	@Test
	public void TestEditCheck() throws Exception{
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		inventoryPojo i2=InvInitialise2();
		orderitemPojo oi1=new orderitemPojo();
		oi1.setBarcode(i2.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i2.getQuantity()-5);
		item.add(oi1);
		service.AddItems(item);
		int order_id=service.getAll().get(0).getId();
		orderitemPojo oi2=service.get(order_id).get(0);
		int quantity=oi2.getQuantity();
		oi2.setPrice(1);
		oi2.setQuantity(10);
		productPojo p=new productPojo();
		p.setProduct_id(i2.getId());
        assertEquals(10,service.editcheck(oi2,p,quantity));
	}



	@Test
	public void TestUpdateItem() throws Exception{
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		inventoryPojo i2=InvInitialise2();
		orderitemPojo oi1=new orderitemPojo();
		oi1.setBarcode(i2.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i2.getQuantity()-2);
		item.add(oi1);
		service.AddItems(item);
		assertEquals(indao.select(indao.selectAll().get(0).getId()).getQuantity(), 2);
		int order_id=service.getAll().get(0).getId();
		orderitemPojo oi2=service.get(order_id).get(0);
		int quantity=oi2.getQuantity();
		oi2.setPrice(1);
		oi2.setQuantity(i2.getQuantity()-1);
        System.err.print(service.update(oi2.getId(), oi2,quantity));		
		orderitemPojo item1=service.getid(oi2.getId());
		assertEquals(item1.getBarcode(), i2.getBarcode());
		assertEquals(item1.getOrder_id(), order_id);
		assertEquals(item1.getName(), i2.getName());
		assertEquals(item1.getPrice(), oi2.getPrice(),0.01);
		assertEquals(item1.getQuantity(), oi1.getQuantity());
		assertEquals(indao.select(indao.selectAll().get(0).getId()).getQuantity(), 19);
	}

	@Test
	public void TestUpdateWrongItem1() throws Exception{
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		inventoryPojo i2=InvInitialise2();
		orderitemPojo oi1=new orderitemPojo();
		oi1.setBarcode(i2.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i2.getQuantity()-2);
		item.add(oi1);
		service.AddItems(item);
		assertEquals(indao.select(indao.selectAll().get(0).getId()).getQuantity(), 2);
		int order_id=service.getAll().get(0).getId();
		orderitemPojo oi2=service.get(order_id).get(0);
		int quantity=oi2.getQuantity();
		oi2.setPrice(1000);
		oi2.setQuantity(i2.getQuantity()-1);
		oi2.setBarcode("sdd");
		assertEquals(0,service.update(oi2.getId(), oi2,quantity));		
	}

	@Test
	public void TestUpdateWrongItem2() throws Exception{
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		inventoryPojo i2=InvInitialise2();
		orderitemPojo oi1=new orderitemPojo();
		oi1.setBarcode(i2.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i2.getQuantity()-2);
		item.add(oi1);
		service.AddItems(item);
		assertEquals(indao.select(indao.selectAll().get(0).getId()).getQuantity(), 2);
		int order_id=service.getAll().get(0).getId();
		orderitemPojo oi2=service.get(order_id).get(0);
		int quantity=oi2.getQuantity();
		oi2.setPrice(1000);
		oi2.setQuantity(i2.getQuantity()-1);
		exceptionRule.expect(ApiException.class);
	    exceptionRule.expectMessage("Selling Price should not be greater than MRP");
		service.update(oi2.getId(), oi2,quantity);		
	}

	@Test
	public void TestUpdateWrongItem3() throws Exception{
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		inventoryPojo i2=InvInitialise2();
		orderitemPojo oi1=new orderitemPojo();
		oi1.setBarcode(i2.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i2.getQuantity()-2);
		item.add(oi1);
		service.AddItems(item);
		assertEquals(indao.select(indao.selectAll().get(0).getId()).getQuantity(), 2);
		int order_id=service.getAll().get(0).getId();
		orderitemPojo oi2=service.get(order_id).get(0);
		int quantity=oi2.getQuantity();
		oi2.setPrice(1);
		oi2.setQuantity(quantity+4);
		assertEquals(2,service.update(oi2.getId(), oi2,quantity));		
	}

	@Test
	public void TestUpdateWrongItem4() throws Exception{
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		productPojo p=prodInitialise1();
		inventoryPojo i = new inventoryPojo();
		i.setId(p.getProduct_id());
        i.setName("150 g");
		i.setBarcode("1");
		i.setQuantity(10);
		invservice.add(i);
		orderitemPojo oi1=new orderitemPojo();
		oi1.setBarcode(i.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i.getQuantity());
		item.add(oi1);
		service.AddItems(item);
		int order_id=service.getAll().get(0).getId();
		orderitemPojo oi2=service.get(order_id).get(0);
		int quantity=oi2.getQuantity();
		p.setMrp(2000.0);
		oi2.setPrice(1);
		oi2.setQuantity(quantity+4);
		assertEquals(2,service.update(oi2.getId(), oi2,quantity));		
	}

	@Test
	public void TestUpdateItemDelInv() throws Exception{
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		productPojo p=prodInitialise1();
		inventoryPojo i = new inventoryPojo();
		i.setId(p.getProduct_id());
        i.setName("150 g");
		i.setBarcode("1");
		i.setQuantity(10);
		invservice.add(i);
		orderitemPojo oi1=new orderitemPojo();
		oi1.setBarcode(i.getBarcode());
		oi1.setPrice(1);
		oi1.setQuantity(i.getQuantity()-1);
		item.add(oi1);
		service.AddItems(item);
		assertEquals(indao.selectAll().size(), 1);
		int order_id=service.getAll().get(0).getId();
		orderitemPojo oi2=service.get(order_id).get(0);
		int quantity=oi2.getQuantity();
		p.setMrp(1000);
		oi2.setPrice(1);
		oi2.setQuantity(quantity+1);
        assertEquals(1,service.update(oi2.getId(), oi2,quantity));		
		orderitemPojo item1=service.getid(oi2.getId());
		assertEquals(item1.getBarcode(), i.getBarcode());
		assertEquals(item1.getOrder_id(), order_id);
		assertEquals(item1.getName(), i.getName());
		assertEquals(item1.getPrice(), oi2.getPrice(),0.01);
		assertEquals(item1.getQuantity(), oi1.getQuantity());
		assertEquals(indao.selectAll().size(), 0);
	}


	@Test
	public void TestOrderUpdate() throws ApiException{
		orderPojo o=service.create();
		o.setInvoiceGenerated(true);
		service.update(o.getId(), o);
		assertEquals(true, o.isInvoiceGenerated());
	}

	@Test
	public void TestDeleteOrderItem() throws Exception{
		List<orderitemPojo> item=new ArrayList<orderitemPojo>();
		InvInitialise2();
		orderitemPojo oi1=new orderitemPojo();
		oi1.setBarcode("2");
		oi1.setPrice(1);
		oi1.setQuantity(1);
		item.add(oi1);
		service.AddItems(item);
		int order_id=service.getAll().get(0).getId();
		orderitemPojo oi2=service.get(order_id).get(0);
		service.delete(oi2.getId());
		List<orderitemPojo> items=service.get(order_id);
		assertEquals(0, items.size());
	}
	
}
