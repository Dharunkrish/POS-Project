package com.increff.employee.service;

import static org.junit.Assert.assertEquals;
import org.junit.rules.ExpectedException;
import org.junit.Rule;
import java.util.List;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.UserPojo;
import com.increff.employee.pojo.brandPojo;


public class UserServiceTest extends AbstractUnitTest {

	@Autowired
	private UserService service;

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
    
	public UserPojo Initialise1()  throws ApiException {
		UserPojo p = new UserPojo();
        p.setEmail("dharnid109@gmail.com");
        p.setPassword("123456789");
		p.setRole("supervisor");
		service.add(p);
		return p;
	}

	public UserPojo Initialise2()  throws ApiException {
		UserPojo p = new UserPojo();
        p.setEmail("niddhar59@gmail.com");
        p.setPassword("987654321");
		p.setRole("operator");
		service.add(p);
		return p;
	}

	@Test
	public void TestAdd() throws ApiException {
		UserPojo p = new UserPojo();
		p.setEmail("dharnid109@gmail.com");
        p.setPassword("123456789");
		p.setRole("supervisor");
		service.add(p);
		p=service.getAll().get(0);
		assertEquals("dharnid109@gmail.com", p.getEmail());
		assertEquals("123456789", p.getPassword());
		assertEquals("supervisor", p.getRole());
	}

	
	@Test
	public void TestNormalize() {
		UserPojo p = new UserPojo();
		p.setEmail(" dharniD109@gmail.com  ");
		p.setRole(" Supervisor ");
		UserService.normalize(p);
		assertEquals("dharnid109@gmail.com", p.getEmail());
		assertEquals("supervisor", p.getRole());
	}

	@Test
	public void TestGetEmail() throws ApiException{
		Initialise1();
		UserPojo p=service.get("dharnid109@gmail.com");
		assertEquals("dharnid109@gmail.com", p.getEmail());
		assertEquals("123456789", p.getPassword());
		assertEquals("supervisor", p.getRole());
	}

	@Test
	public void TestGetAll() throws ApiException{
		Initialise1();
		Initialise2();
		List<UserPojo> list=service.getAll();
		UserPojo p1=list.get(0);
		assertEquals("dharnid109@gmail.com", p1.getEmail());
		assertEquals("123456789", p1.getPassword());
		assertEquals("supervisor", p1.getRole());
		UserPojo p2=list.get(1);
		assertEquals("niddhar59@gmail.com", p2.getEmail());
		assertEquals("987654321", p2.getPassword());
		assertEquals("operator", p2.getRole());
	}

	@Test
	public void Testdelete() throws ApiException{
		Initialise1();
		int id=service.getAll().get(0).getId();
        service.delete(id);
		assertEquals(0, service.getAll().size());
	}

	@Test
	public void TestAddExistingUser() throws ApiException{
		Initialise1();
		UserPojo p=new UserPojo();
		p.setEmail("dharnid109@gmail.com");
        p.setPassword("123456789");
		p.setRole("supervisor");
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("User with given email already exists");
        service.add(p);
		}

	@Test
	public void TestOtherRole() throws ApiException{
		UserPojo p=new UserPojo();
		p.setEmail("dharnid109@gmail.com");
        p.setPassword("123456789");
		p.setRole("admin");
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Role must be Supervisor or Operator");
        service.add(p);
	}

	@Test
	public void TestUpdate() throws ApiException{
		Initialise1();
		UserPojo p=service.getAll().get(0);
		p.setEmail("niddhar59@gmail.com");
        p.setPassword("987654321");
		p.setRole("supervisor");
        service.update(p,p.getId());
		p=service.getAll().get(0);
		assertEquals("niddhar59@gmail.com", p.getEmail());
		assertEquals("987654321", p.getPassword());
		assertEquals("supervisor", p.getRole());
	}

	@Test
	public void TestWrongUpdate() throws ApiException{
		Initialise1();
		Initialise2();
		UserPojo p=new UserPojo();
		int id=service.getAll().get(0).getId();
		p.setEmail("niddhar59@gmail.com");
        p.setPassword("987654321");
		p.setRole("supervisor");
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("User with Email already Present");
        service.update(p,id);
	}

	@Test
	public void TestWrongRoleUpdate() throws ApiException{
		Initialise1();
		Initialise2();
		UserPojo p=service.getAll().get(0);
		p.setEmail("niddhar59@gmail.com");
        p.setPassword("987654321");
		p.setRole("admin");
		exceptionRule.expect(ApiException.class);
		exceptionRule.expectMessage("Role must be Supervisor or Operator");
        service.update(p,p.getId());
	}



}
