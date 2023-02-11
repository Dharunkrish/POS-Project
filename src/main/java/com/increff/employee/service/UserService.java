package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.UserDao;
import com.increff.employee.pojo.UserPojo;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	@Transactional
	public void add(UserPojo p) throws ApiException {
		normalize(p);
		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			throw new ApiException("User with given email already exists");
		}
		if (p.getRole()!="supervisor" && p.getRole()!="operator"){
			throw new ApiException("Role must be Supervisor or Operator");
		}
		dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}
	
	@Transactional
	public void update(UserPojo u,int id) throws ApiException {
		if (u.getRole()!="supervisor" && u.getRole()!="operator"){
			throw new ApiException("Role must be Supervisor or Operator");
		}
		UserPojo v=get(u.getEmail());
		System.err.print(id);
		System.err.print(v.getId());
		if (v==null || v.getId()==id ) {
			dao.update(u);
		}
		else {
			throw new ApiException("User with Email already Present");
		}
	}

	protected static void normalize(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
		p.setRole(p.getRole().toLowerCase().trim());
	}
}
