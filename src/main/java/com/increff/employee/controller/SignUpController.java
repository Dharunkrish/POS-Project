package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.increff.employee.model.InfoData;
import com.increff.employee.model.SignUpForm;
import com.increff.employee.pojo.UserPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.UserService;
import com.increff.employee.util.SecurityUtil;
import com.increff.employee.util.UserPrincipal;

import io.swagger.annotations.ApiOperation;

@Controller
public class SignUpController {

	@Autowired
	private UserService service;
	@Autowired
	private InfoData info;
	
	@Value("#{'${SupervisorEmail}'.split(',')}")
	List<String> SupervisorEmail=new ArrayList<String>();
	
	private Logger logger = Logger.getLogger(BrandApiController.class);

	@ApiOperation(value = "Sign up a new user")
	@RequestMapping(path = "/session/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView signup(HttpServletRequest req, SignUpForm f) throws ApiException {
		logger.info(f.getEmail());
		UserPojo p = service.get(f.getEmail());
        if (p!=null) {
			info.setMessage("User Already Present. Redirecting to the login page");
    		return new ModelAndView("redirect:/site/login");
        }
		UserPojo user=convert(f);
		service.add(user);
		// Create authentication object
		Authentication authentication = convert(user);
		// Create new session
		HttpSession session = req.getSession(true);
		// Attach Spring SecurityContext to this new session
		SecurityUtil.createContext(session);
		// Attach Authentication object to the Security Context
		SecurityUtil.setAuthentication(authentication);
		return new ModelAndView("redirect:/ui/home");
	}
	
	 private UserPojo convert(SignUpForm signupForm) throws ApiException {
	        //set the role
		    UserPojo user = new UserPojo();
	        user.setEmail(signupForm.getEmail());
	        user.setPassword(signupForm.getPassword());
	        user.setRole("operator");
	        logger.info(SupervisorEmail);
	        for(String email : SupervisorEmail){
            	logger.info(email);
	            if(email.equals(signupForm.getEmail())){
	                user.setRole("supervisor");
	                break;
	            }
	        }
	        return user;
	    }


	private static Authentication convert(UserPojo p) {
		// Create principal
		UserPrincipal principal = new UserPrincipal();
		principal.setEmail(p.getEmail());
		principal.setId(p.getId());
		principal.setRole(p.getRole());


		// Create Authorities
		ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(p.getRole()));
		// you can add more roles if required

		// Create Authentication
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
				authorities);
		return token;
	}

}
