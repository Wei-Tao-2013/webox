package com.webox.common.utils;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;


public class AppData implements Serializable {
	
	private static final long serialVersionUID = 511637120741623364L;

	public static HashMap<String,String> cityCode = new HashMap<String,String>();
	
	public static HashMap<String,String> serviceType = new HashMap<String,String>();

	public static Map<String,String> searchingService = new HashMap<String,String>();

	public static String serviceFunction = "false";
		
}