package com.webox.common.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor

public class Order {
    private @Id String orderId;
    @Indexed
    private String userId; // User.userId
    private String serviceType; // value is ServiceType.typeId
    @Indexed
    private String serviceId;
    @Indexed
    private String quoteId;
    @Indexed
    private String vendorId;
    @Indexed
    private String jobId;
    @Indexed
    private int Status; // 0, Booked, 1 Completed , 2, Cancel
    private LocalDateTime orderCompletedTime; // the time complete the order
    private LocalDateTime orderBookedTime;
    private LocalDateTime orderCancelTime;
    @Indexed
    private int managementStatus = 0; // 0, nothing, 1, complained , 2, soleved
    private LocalDateTime managementStatusChangeTime;
    private int feedbackRate; // [1,5]
    private String feedback;
    private LocalDateTime feedbackTime;
    private String response;
    private LocalDateTime responseTime;

}