/*
 * Copyright (c) 2013, Queen's Printer of British Columbia, Canada and/or its affiliates. 
 * All rights reserved. DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE Header.
 * 
 * Please contact Queen's Printer of British Columbia, PO Box 9452 Stn Prov Govt, Victoria 
 * BC, V8W 9V7, (250) 387-3309 if you have any questions or have received this class in 
 * error.
 * 
 */

package gov.ca.bc.qp.QPDefender.config;

/**
 * Unfortunately enums do not play well with RolesAllowed annotations so I was forced
 * to use a list of static final variables instead. This class keeps a list of the roles
 * available to this application.
 * @author spencer.tickner
 */
public class MyRoles {
	/**
	 * Shared QPAdmin role that basically acts as the administrator account on all qp systems.
	 */
	public static final String QP_ADMIN = "qpadmin";
	/**
	 * Guest account that others can access to update and retrieve there own password and 
	 * 	account information.
	 */
	public static final String QP_SECURITY_GROUP_ADMIN = "security.group.admin";
	
	/**
	 * User account that can only update their own credential information.
	 */
	public static final String QP_SECURITY_USER = "security.user";
	
	
}
