package com.increff.employee.spring;

import java.time.Period;
import java.time.ZonedDateTime;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.increff.employee.model.reportForm;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.reportService;

@Configuration
@EnableScheduling

public class Scheduler extends WebMvcConfigurerAdapter {
	
	private static Logger logger = Logger.getLogger(SecurityConfig.class); 
	
	@Autowired
	private reportService service;
	
	@Scheduled(cron = "40 43 10 * * *")
	public void scheduleTaskUsingCronExpression() throws ApiException{
		logger.info("hello");
		service.add();
	}
	
	@Bean
	public TaskScheduler  taskScheduler() {
	    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
	    threadPoolTaskScheduler.setPoolSize(4);
	    threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
	    return threadPoolTaskScheduler;
	}
}