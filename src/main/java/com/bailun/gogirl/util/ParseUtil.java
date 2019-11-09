package com.bailun.gogirl.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bailun.gogirl.bean.Customer;

public class ParseUtil {
	private static Logger  logger = LoggerFactory.getLogger(ParseUtil.class);
	
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}
	public static Map<String, Object> paramsToMap(Object o){
		Map<String, Object> map = new HashMap<>();
		try {
	        Class<? extends Object> c = o.getClass();
	        Field[] fields = c.getDeclaredFields();
	        for (Field field : fields) {
	            field.setAccessible(true);
	            String name = field.getName();
	            Object value = field.get(o);
	            if (value != null){
//	            	if(value instanceof Date){
//	            		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	            		map.put(name, sdf.format((Date)value));
//	            	}else{
	            		map.put(name, String.valueOf(value));
//	            	}
	            }
	                
	        }
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return map;
	}

//	public static void main(String[] o) {
//		Customer customer = new Customer();
//		customer.setId(123);
//		customer.setCity("abc");
//		try {
//			Map<String, Object> map = ParseUtil.paramsToMap(customer);
//			System.out.println(map);
//		} catch (IllegalAccessException | IllegalArgumentException
//				| SecurityException e) {
//
//			e.printStackTrace();
//		}
//	}
}
