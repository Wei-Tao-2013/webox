package com.webox.common.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
@Document
@Data  @NoArgsConstructor
public class HotSpot{
   private @Id String  hotSpotId;
   @Indexed
   private String userId;
   private String hotSpotType;   // 'Service' , 'Others'
   @Indexed
   private ObjectId serviceId;
  // private String serviceId;
   private Double rank = new Double(0.0001);
   private Boolean status; // true, false
   private LocalDateTime  updateTime;
   
   public HotSpot(String userId, String serviceId){
    this.serviceId = new ObjectId(serviceId);
    //this.serviceId =  serviceId;
    this.userId = userId;
   }

   public String getServiceId(){
       return this.serviceId.toHexString();
   }
}