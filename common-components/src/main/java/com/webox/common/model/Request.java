package com.webox.common.model;

import java.util.List;

import lombok.Data;

@Data
public class Request implements java.io.Serializable {

	/** For serialization. */
	private static final long serialVersionUID = -8980734683931860246L;

	private String referralId;
	private String referralType; // vendorType or service
	private String tokenSeed;
	/// user info
	private User user;
	/// account info
	private Account account;

	//// wechat info
	private String weixinCode;
	private String rawData;
	private RawData rawDataobj;
	private String encryptedData;
	private String signature;
	private String iv;

	// Service info
	private String serviceId;
	private Service service;
	private String userId;
	private String serviceStatus;
	private String fileId;
	private String tag; // search keyword

	// Job info
	private Job job;
	private String jobId;
	private String jobStatus; // 0 jobCreated, 1,jobUpdated, 2 jobDone ,3 jobCancel
	private String vendorId;

	// Quote info
	private Quote quote;
	private String quoteId;
	private String quoteStatus; // 0, Booked, 1 Completed , 2, Cancel
	private String priceDesc;
	private String quoteReply;

	// Order info
	private Order order;
	private String orderId;
	private String orderStatus;

	// hotspot
	private List<String> serviceIdList;

	// PostInfo
	private String postInfoId;
	private PostInfo postInfo;
	private String postStatus;
	private List<String> postIdList;

	private int limit;

}