package com.webox.common.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageCount {
	// private String serviceId;
	private int total; // unread msg in total;
	private String userId;
	private LocalDateTime lastMsgTime;
	private String lastMsg;
	private String avatar;
	private String serviceType;
	private String serviceCity;
	private String userName;
	private int vendorInd; // 0 vendor , 1, customer
	private Service service;
	private List<Service> services;

}