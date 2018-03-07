/*
 * Copyright (c) 2013, Queen's Printer of British Columbia, Canada and/or its affiliates. 
 * All rights reserved. DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE Header.
 * 
 * Please contact Queen's Printer of British Columbia, PO Box 9452 Stn Prov Govt, Victoria 
 * BC, V8W 9V7, (250) 387-3309 if you have any questions or have received this class in 
 * error.
 * 
 */
package gov.ca.bc.qp.QPDefender.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Listener for registering log4j 
 * output as defined in the log4j.properties file
 * located in the resources folder.
 */
public class ApplicationServletContextListener implements ServletContextListener
{
	
	static final Logger log = Logger.getLogger(ApplicationServletContextListener.class);
	
    public void contextInitialized(ServletContextEvent event) 
    { 
	ServletContext ctx = event.getServletContext();
	log.info("Initializing QPDefender Application");
	String prefix =  ctx.getRealPath("/");     
	String file = "WEB-INF"+System.getProperty("file.separator")+"classes"+System.getProperty("file.separator")+"log4j.properties";

	if(file != null) {
	    PropertyConfigurator.configure(prefix+file);
	}   
    }

    public void contextDestroyed(ServletContextEvent event){ /* ignore */}

}
