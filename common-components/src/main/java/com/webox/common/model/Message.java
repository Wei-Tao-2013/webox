package com.webox.common.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="message")
@Data @NoArgsConstructor

public class Message {
    private @Id String msgId;
    @Indexed
    private ObjectId sender;  // User.userId message sender 
    @Indexed
    private ObjectId receiver; // User.userId message receiver
    @Indexed
    private ObjectId serviceId; // service.serviceId
    private String msgType;  // 'text','image','voice'
    private String msgChannel; // 'service','notifciation','others'
    @Indexed
    private int msgStatus; // 1 - 'sent', 0 - 'inQ', 2 -'block out'
    private int msgInQReason; // '1'- 'receiver off line', 2 -'websocket disconnened', 3 -'excetpions'
    private String msgSentException; // if exception
    private String msgContent;
    private String msgSentIp; // ip address where msg been sent by user
    private LocalDateTime inComeTime;  // time of message income to server
    private LocalDateTime sendOutTime;  // time of message sent out from server
    private int trytoSendRound = 0;  // recoder times until sending out 
    private String msgPayload;

    
   
}