package gov.ca.bc.qp.QPDefender.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.ca.bc.qp.qpcommon.code.QPBean;

public class ObjectUtil {

	static Logger log = LogManager.getLogger(ObjectUtil.class);
	private static Date emptyDate = null;
	
	public static Date getEmptyDate() {
		if(emptyDate == null) {
			 try {
				emptyDate = (new SimpleDateFormat("dd/MM/yyyy")).parse("05/11/1900");
			} catch (ParseException e) {
				// This should never happen, log it just in case.
				log.error("Error parsing our empty date", e);
			}
		}
		return emptyDate;
	}
	
	
	
	public static boolean equal(String arg1, String arg2) {
		boolean equal = false;
		if(arg1 == null && arg2 == null)
			equal = true;
		else if(arg1 == null || arg2 == null)
			equal = false;
		else
			equal = arg1.equals(arg2);
		
		return equal;
	}
	
	public static boolean equal(boolean arg1, boolean arg2) {
		return (arg1 == arg2);
	}
	
	public static boolean equal(int arg1, int arg2) {
		return (arg1 == arg2);
	}
	
	public static boolean equal(Date arg1, Date arg2) {
		boolean equal = false;
		if(arg1 == null && arg2 == null)
			equal = true;
		else if(arg1 == null || arg2 == null)
			equal = false;
		else
			equal = (arg1.compareTo(arg2) == 0);
		return equal;
	}
	
	public static boolean equal(List<? extends QPBean> arg1, List<? extends QPBean> arg2) {
		boolean equal = false;
		if(arg1 == null && arg2 == null) 
			equal = true;
		else if(arg1 == null || arg2 == null)
			equal = false;
		else if(arg1.size() != arg2.size())
			equal = false;
		else {
			equal = true;
			for(int i = 0; i < arg1.size(); i++) {
				if(!arg1.get(i).isEqual(arg2.get(i))) {
					equal = false;
					break;
				}
			}
		}
		return equal;
	}
	
	public static boolean equal(QPBean arg1, QPBean arg2) {
		return arg1.isEqual(arg2);
	}
	public static boolean equal(Integer arg1, Integer arg2) {
		boolean equal = false;
		if(arg1 == null && arg2 == null)
			equal = true;
		else if(arg1 == null || arg2 == null)
			equal = false;
		else
			equal = arg1.equals(arg2);
		
		return equal;
	}
	

}
