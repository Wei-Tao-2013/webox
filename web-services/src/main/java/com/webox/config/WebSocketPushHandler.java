package com.webox.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.webox.common.model.Message;
import com.webox.common.process.MessageManage;

import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketPushHandler implements WebSocketHandler {
  
  private static final Logger logger = LoggerFactory.getLogger(WebSocketPushHandler.class);
  private static final List<WebSocketSession> users = new ArrayList<>();
  
  @Autowired  @Qualifier("messageManage")
  MessageManage messageManage;

  // User be able to listen the webscoket message 
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    //System.out.println("login system ...");
    String userId = "unknown";
    String otherId= "unknown";
    String queryStr = session.getUri().getQuery();
    if (queryStr.length() > 0 ){
      String[] queryArray = queryStr.split("&"); 
      if (queryArray.length > 0 ){
          userId = queryArray[0].split("=")[1];
          otherId = queryArray[1].split("=")[1];
      }
    }
   // System.out.println(userId + " has connected to websocket ... ");
    logger.debug(" {} user has connected to websocket ... ",userId);
    session.getAttributes().put("userId", userId);
    session.getAttributes().put("otherId", otherId);
    users.add(session);
    // retrive unread message 
    this.retrieveMsg(userId, otherId);
  
  }

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
   // System.out.println( "receive message from user "+message.getPayload());
    logger.debug( "receive message from user {} by session address {} " , message.getPayload(),session.getRemoteAddress() );
    HashMap<String, String> map = this.convertPayloadtoMap(message.getPayload().toString());
    // add reomte address
    map.put("msgSentIp",session.getRemoteAddress().toString());
    Message messageObj = messageManage.constructMessageObj(map);       
    sendMessageToUser(messageObj,new TextMessage(message.getPayload().toString()));
  }

  // convert payload to hashmap
  private HashMap<String,String> convertPayloadtoMap(String payLoad){
    HashMap<String, String> map = new HashMap<String, String>();
    JSONObject jObject = new JSONObject(payLoad);
    Iterator<?> keys = jObject.keys();
    while (keys.hasNext()) {
      String key = (String) keys.next();
      String value = jObject.get(key).toString();
      map.put(key,value);
    }
    map.put("msgPayload",payLoad);
    return map;
  }

  // handle error process
  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    logger.error("message send user  {} coming from IP {} ", session.getAttributes().get("userId"), session.getRemoteAddress());  
    System.out.println( "error occured with user "+session.getAttributes().get("userId") );
  }

  // when user exited should release session resource.
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    if (session.isOpen()) {
      session.close();
    }
    users.remove(session);
    System.out.println("user exited from socket session");
  }

  @Override
  public boolean supportsPartialMessages() {
    return false;
  }
  
  /**
   * Send message to all users
   */
  private void sendMessagesToUsers(TextMessage message) {
     users.forEach(user->{
      logger.debug("user session id {} user id {} ", user.getId(), user.getAttributes().get("userId"));  
     });
    for (WebSocketSession user : users) {
      try {
        // isOpen() is onine
        if (user.isOpen()) {
          user.sendMessage(message);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
  * send message to specify user
  */
  private void sendMessageToUser(Message msg,TextMessage message) {
    // msgStatus; // 1 - 'sent', 0 - 'inQ', 2 -'block out'
    // msgInQReason; // '1'- 'receiver off line', 2 -'websocket disconnened', 3 -'excetpions'
    String receiver = msg.getReceiver().toHexString();
    List<WebSocketSession> userList = users.stream().filter(user->receiver.equals(user.getAttributes().get("userId"))).collect(Collectors.toList());
    int sentTimes = msg.getTrytoSendRound() + 1;
    LocalDateTime localDateTime = LocalDateTime.now();
    if (!userList.isEmpty()) {
        WebSocketSession user = userList.get(0);
        try {
            if (user.isOpen()){
              msg.setSendOutTime(localDateTime);
              msg.setMsgStatus(1);//  sent out
              msg.setTrytoSendRound(sentTimes);
              messageManage.saveMessage(msg);
              user.sendMessage(message);
              System.out.println("Sent out to receiver " + receiver + " with message " + message);
            } else {  // if user lost connection process 
              msg.setMsgStatus(0);// in Q
              msg.setMsgInQReason(2);//websocket disconnnected
              msg.setSendOutTime(null); // yet to be sent
              msg.setTrytoSendRound(sentTimes);
              messageManage.saveMessage(msg);
              System.out.println("message is in Q for receiver as websocket disconnected " + receiver + " with message " + message);
            }
        }catch (IOException e) {
            e.printStackTrace();
            logger.error("send message to user {} is failed caused by {} ", receiver, e.getMessage());
            msg.setMsgStatus(0);// in Q
            msg.setMsgInQReason(3);// exceptions
            msg.setTrytoSendRound(sentTimes);
            msg.setSendOutTime(null); // yet to be sent
            msg.setMsgSentException(e.getMessage().substring(0,100));
            messageManage.saveMessage(msg);
            System.out.println("message is in Q as exception for " + receiver + " with message " + message);
            // process of message 
          }
        } else { // if the user is off line process 
            msg.setMsgStatus(0);// in Q
            msg.setMsgInQReason(1);// off line
            msg.setTrytoSendRound(sentTimes);
            msg.setSendOutTime(null); // yet to be sent
            messageManage.saveMessage(msg);
            System.out.println("message is in Q for as receiver off line " + receiver + " with message " + message);
        }
    }
    
  // when user is back online will send out the message which in Q or failed sending out before;
  private void retrieveMsg(String userId){
    List<Message> messageList = messageManage.retrieveMessagebyUserId(userId);
    System.out.println("start to retrieve Msg for receiver " + userId + " with message number  " + messageList.size());
    messageList.forEach(msg->{
      this.sendMessageToUser(msg, new TextMessage(msg.getMsgPayload()));
    });
  }

  // when user is back online will send out the message which in Q or failed sending out before as per sender id
  private void retrieveMsg(String userId, String senderId){
    List<Message> messageList = messageManage.retrieveMessagebyUserIdandSenderId(userId, senderId);
    System.out.println("start to retrieve Msg for receiver " + userId + " from "+ senderId+" with message number  " + messageList.size());
    messageList.forEach(msg->{
      this.sendMessageToUser(msg, new TextMessage(msg.getMsgPayload()));
    });
  }


};
    
   
   