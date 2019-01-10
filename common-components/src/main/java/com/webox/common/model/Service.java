package com.webox.common.model;

import java.time.LocalDate;
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
public class Service {
    
    private @Id String serviceId;
    private String serviceType;  // refer to ServiceType
    @Indexed
    private String serviceName; 
    private int serviceCategory = 0; // 0 = general , 1 = activity , 2 = info
    private int serviceOrderProcess = 3; // 0 = none  , 1 = customer , 2 = customer-vendor, 3 = customer-vendor-customer   
    private String keywords;  
    private List<Photo> servicePhoto = new ArrayList<Photo>();
    private String shortDesc;
    private String longDesc;
    private String priceDesc;
    private String serviceLevel;
    private String serviceFDR;
    private LocalDate serviceStartDate;
    private LocalDateTime serviceRegisterDate;
    private String certified;
    private LocalDateTime certifiedTime;
    private String status;
    private LocalDateTime updatedTime;
    private String additionalInfo;
    private int sortNumber;
    private Double rank = new Double(0.0001);

    private int reviewNumber;   
    private int subscribedNumber;
    private int orderNumber;
    private int positiveCmtsNum;
    private int nagativeCmtsNum;
    private int neutralCmtsNum;

    @Indexed
    private String userId;  // User.userId
    private Contact contact;
    private Address address;

    public Service addServicePhoto(Photo photo) {
        this.servicePhoto.add(photo);
        return this;
    }

    public Service deleteServicePhoto(Photo photo){
       return this;
    }

}