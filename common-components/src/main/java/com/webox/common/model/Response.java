package com.webox.common.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Response implements java.io.Serializable {

	/** For serialization. */
	private static final long serialVersionUID = -8980734683931860246L;
	private String appErr;
	private String appInfo;
	private String appStatus;
	// for message
	private AppMessage appMessage;

	private String appCode;
	private User user;
	private Account account;
	private List<User> users;
	private String token;
	// for service
	private List<Service> services;
	private boolean beWatched;
	private String serviceId;
	private Service service;
	// for file upload response
	private UploadFileResponse uploadFileResponse;
	private List<Photo> photoList;
	private List<String> FileIdList;
	private String fileId;

	// for hotSpot
	private List<HotSpot> hotSpotList;

	// for job
	private String jobId;
	private List<Job> jobList;
	private Job job;

	// for quote
	private String quoteId;
	private List<Quote> quoteList;
	private Quote quote;
	private Map<String, List<Quote>> quoteGroup;

	// for order
	private String orderId;
	private List<Order> orderList;
	private Order order;
	private Map<String, List<Order>> orderGroup;

	// for watchList
	private List<WatchList> watchList;

	// for message
	private List<MessageCount> messageCountList;

	// for post info
	private List<PostInfo> postInfos;
	private String postInfoId;
	private PostInfo postInfo;

}