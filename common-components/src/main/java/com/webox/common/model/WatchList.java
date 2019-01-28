package com.webox.common.model;

import java.time.LocalDateTime;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "watchList")
@Data
@NoArgsConstructor
public class WatchList {
    private @Id String watchListId;
    @Indexed
    private String userId;
    private String watchListType; // 'Service' , 'Others'
    @Indexed
    private ObjectId serviceId;
    // private String serviceId;
    private Double rank = new Double(0.0001);
    private Boolean status; // true, false
    private LocalDateTime updateTime;

    public WatchList(String userId, String serviceId) {
        this.serviceId = new ObjectId(serviceId);
        // this.serviceId = serviceId;
        this.userId = userId;
    }

    public String getServiceId() {
        return this.serviceId.toHexString();
    }
}