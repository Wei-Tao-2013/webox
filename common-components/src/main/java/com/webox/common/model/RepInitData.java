package com.webox.common.model;

import java.util.HashMap;

import lombok.Data;

@Data
public class RepInitData implements java.io.Serializable {

	/** For serialization. */
	private static final long serialVersionUID = 1339437625847806181L;

	private String appErr;
	private String appInfo;
	private String appStatus;

	private HashMap<String, String> cityCode;
	private HashMap<String, String> serviceType;

	private String serviceFunction;

}