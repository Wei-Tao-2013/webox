package com.webox.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.google.gson.Gson;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class GIutils {

    public static String converToJson(Object obj) {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(obj);
		return jsonStr;
	}

	public static Object copyProperties(Object fromObj, Object toObj) {
		if (fromObj != null) {
			BeanWrapper bw = new BeanWrapperImpl(fromObj);
			BeanUtils.copyProperties(bw.getWrappedInstance(), toObj);
		} else {
			toObj = null;
		}
		return toObj;
	}

	public static String getDatebyFormat(LocalDateTime date){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       return df.format(date);
      
	}
}