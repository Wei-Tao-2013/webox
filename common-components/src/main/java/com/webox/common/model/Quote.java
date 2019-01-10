package com.webox.common.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data @NoArgsConstructor

public class Quote {
   
    private @Id String quoteId;
    @Indexed
    private String userId;  // User.userId
    @Indexed
    private String serviceId; // Service.serviceId
    @Indexed
    private String jobId;// Job.jobId;
    @Indexed
    private String vendorId; // service vendor;
    private String serviceType;  // 
    private String quoteContent; // any explation attached
    private String quoteReply; // by vendor 
    private BigDecimal price;  // quote price 
    private String priceDesc;// price desription in general 
    private LocalDateTime serviceStartTime;
    private LocalDateTime serviceEndTime;
    private LocalDateTime quoteRequestSentTime;
    private LocalDateTime quotePriceSentTime;
    private LocalDateTime quoteAcceptTime;
    private LocalDateTime quoteExpiredTime;
    private LocalDateTime quoteRjectedTime;
    private LocalDateTime quoteUpdateTime;
    @Indexed
    private int status;  	// 0 QuoteSent, 1 QuotePriceSent, 2 QuoteAccept 3 QuoteExpired 4, QuoteRejected 
    
}