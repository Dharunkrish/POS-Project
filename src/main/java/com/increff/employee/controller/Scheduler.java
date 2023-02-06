package com.increff.employee.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.increff.employee.service.ApiException;
import com.increff.employee.service.reportService;

@Configuration
@EnableScheduling

public class Scheduler extends WebMvcConfigurerAdapter {
	
	private static Logger logger = Logger.getLogger(Scheduler.class); 
	
	@Autowired
	private reportService service;
	
	@Scheduled(cron = "10 0 0 * * *")
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