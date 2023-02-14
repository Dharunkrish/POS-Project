package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.UserData;
import com.increff.employee.model.UserForm;
import com.increff.employee.pojo.UserPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.UserService;
import com.increff.employee.util.DataConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class AdminApiController {

	@Autowired
	private UserService service;

	@ApiOperation(value = "Adds a user")
	@RequestMapping(path = "/api/supervisor/user", method = RequestMethod.POST)
	public void addUser(@RequestBody UserForm form) throws ApiException {
		UserPojo p = DataConversionUtil.convert(form);
		service.add(p);
	}

	@ApiOperation(value = "Deletes a user")
	@RequestMapping(path = "/api/supervisor/user/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets list of all users")
	@RequestMapping(path = "/api/supervisor/user", method = RequestMethod.GET)
	public List<UserData> getAllUser() {
		List<UserPojo> list = service.getAll();
		List<UserData> list2 = new ArrayList<UserData>();
		for (UserPojo p : list) {
			list2.add(DataConversionUtil.convert(p));
		}
		return list2;
	}
	

	@ApiOperation(value = "Adds a user")
	@RequestMapping(path = "/api/supervisor/user/{id}", method = RequestMethod.PUT)
	public void updateUser(@PathVariable int id,@RequestBody UserForm form) throws ApiException {
		UserPojo p = DataConversionUtil.convert(form);
		service.update(p,id);
	}



}
