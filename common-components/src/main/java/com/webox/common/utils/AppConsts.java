package com.webox.common.utils;

import java.io.Serializable;
import java.util.HashMap;

import lombok.Data;

@Data
public class AppConsts implements Serializable {
	/** For serialization. */
	private static final long serialVersionUID = 7452102852294401775L;

	/** System Exceptions **/
	public static final String ERROR__EXCETPION = "0999";

	/** Business errors **/
	public static final String ERROR_CODE_LoginIDNotFound = "0090";

	/** Application Status Setting **/
	public static final String RETURN_TRUE = "TRUE";
	public static final String RETURN_FALSE = "FALSE";
	public static final String RETURN_UNKNOW = "UNKNOW";

	// 00 means account register process
	public static final String REG_REGISTERSUCESS = "00-001";
	public static final String REG_USEREXIST = "00-002";
	public static final String REG_ACCEXIST = "00-003";
	public static final String REG_REGSTIERFAILED = "00-004";
	public static final String REG_CCOUNTNOTFOUND = "00-005";
	public static final String REG_SINGINSUCESS = "00-006";
	public static final String REG_SINGOUT = "00-007";
	public static final String REG_USERNOTFOUND = "00-008";
	public static final String REG_USEUPDATED = "00-009";
	// 01 service register process
	public static final String SEV_DRAFTSUCESS = "01-001";
	public static final String SEV_WAITFORAPPROVAL = "01-002";

	// 02 job and quote process
	public static final String JOB_CANNOT_UPDATED = "02-001";
	public static final String QUOTE_CANNOT_UPDATED = "02-002";


	public static final String QUOTE_UPDATED_SUCCESS = "QUOTE_UPDATED_SUCCESS";
	public static final String QUOTE_SENT ="QUOTE_SENT";
	public static final String QUOTE_NOT_FOUND = "QUOTE_NOT_FOUND";
	public static final String JOBLIST_LOADED = "JOBLIST_LOADED";
	public static final String QUOTES_LOADED = "QUOTES_LOADED";
	public static final String QUOTE_PRICE_SENT = "QUOTE_PRICE_SENT";
	public static final String QUOTE_ACCEPT = "QUOTE_ACCEPT";
	public static final String QUOTE_REJECTED ="QUOTE_REJECTED";
	public static final String QUOTE_EXPIRED = "QUOTE_EXPIRED";
	// 99 application level process
	public static final String APP_REFRESHTOKEN = "01-001";

	/** Common explanation **/
	public static final String CALL_MESSAGE = "";

	/** status list **/
	public static final String REJECTED = "Rejected";
	public static final String COMPLETED = "Completed";

	public static enum accountType {
		WECHAT, APPACC, WEBACC, OTHERACC
	};

	// public static enum appStatus {TRUE,FALSE,UNKNOWN};
	public static enum accountStatus {
		CREATED, SIGNIN, SINGOUT, SUSPEND, BLOCKED, OTHERS
	};

	public static enum registerInfo {
		REGISTERSUCESS, USEREXIST, ACCEXIST, REGSTIERFAILED, ACCOUNTNOTFOUND, SINGINSUCESS, SINGOUT
	};

	public static enum userRole {
		CUSTOMER, VENDOR, GI_ADMIN
	};

	public static enum gender {
		MALE, FEMALE, UNKNOWN
	};

	private HashMap<String, Integer> jobStatusHashMap;
	private HashMap<String, Integer> quoteStatusHashMap;

	public AppConsts() {
		// 0 JobCreated, 1,JobUpdated, 2 JobDone ,3 JobCancel, 99 JobAll
		jobStatusHashMap = new HashMap<String, Integer>();
		jobStatusHashMap.put("JobCreated", 0);
		jobStatusHashMap.put("JobUpdated", 1);
		jobStatusHashMap.put("JobAssigned", 2);
		jobStatusHashMap.put("JobCancel", 3);
		jobStatusHashMap.put("JobAll", 99);

		// 0 QuoteSent, 1 QuotePriceSent, 2 QuoteAccept 3 QuoteExpired 4, QuoteRejected 99, QuoteAll
		quoteStatusHashMap = new HashMap<String, Integer>();
		jobStatusHashMap.put("QuoteSent", 0);
		jobStatusHashMap.put("QuotePriceSent", 1);
		jobStatusHashMap.put("QuoteAccept", 2);
		jobStatusHashMap.put("QuoteExpired", 3);
		jobStatusHashMap.put("QuoteRejected", 4);
		jobStatusHashMap.put("QuoteAll", 99);

	}

}