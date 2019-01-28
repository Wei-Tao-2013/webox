package com.webox.common.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor

public class Job {
    private @Id String jobId;
    @Indexed
    private String userId; // User.userId
    private Address address;
    private Contact contact;
    private String serviceType;
    private LocalDateTime jobCreatedTime; //
    private LocalDateTime jobUpdateTime; //
    private LocalDateTime jobStartTime;
    private LocalDateTime jobEndTime;
    @Indexed
    private int status; // 0 JobCreated, 1,JobUpdated, 2 JobAssigned ,3 JobCancel
    private String jobDesc;
    private LocalDateTime updatedTime; //

}