package com.webox.common.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Document
public class QuoteMessage {

    private String message;
    private String messageSender; // service.serviceId or user.userId
    private LocalDateTime sendTime;

    public QuoteMessage(String message, String messageSender) {
        LocalDateTime localDateTime = LocalDateTime.now();
        this.message = message;
        this.messageSender = messageSender;
        this.sendTime = localDateTime;
    }

}
