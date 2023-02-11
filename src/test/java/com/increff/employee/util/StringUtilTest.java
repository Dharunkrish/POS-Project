package com.increff.employee.util;

import static org.junit.Assert.assertEquals;



import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.increff.employee.util.StringUtil;
import com.increff.employee.service.AbstractUnitTest;


public class StringUtilTest extends AbstractUnitTest {


	@Test
	public void TestIsEmpty() {
	     assertEquals(true, StringUtil.isEmpty("       ")); 
		 assertEquals(false, StringUtil.isEmpty("   g    ")); 
	}

	@Test
	public void TestToLower() {
	     assertEquals("sbsf", StringUtil.toLowerCase(" SBsF ")); 
		 assertEquals("", StringUtil.toLowerCase("       ")); 
	}
}
