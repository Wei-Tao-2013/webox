package com.webox.livechat.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.utils.AppConsts;
import com.webox.livechat.service.LiveChatAPI;

@Controller
public class LiveChatController {

    private static final Logger logger = LoggerFactory.getLogger(LiveChatController.class);
    @Autowired 
    private LiveChatAPI liveChatAPI;
 
    @RequestMapping(value="/wb/loadCountUnReadMessage",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response loadCountUnReadMessage(@RequestBody Request request) throws Throwable  {		
        Response response = new Response();
        response.setMessageCountList(liveChatAPI.loadUnreadMsgCount(request.getUserId()));
        response.setAppStatus(AppConsts.RETURN_TRUE);
        return response;
    }

    @RequestMapping(value="/wb/loadMessageAccount",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response loadMessageCount(@RequestBody Request request) throws Throwable  {		
        Response response = new Response();
        response.setMessageCountList(liveChatAPI.loadMessageAccount(request.getUserId()));
        response.setAppStatus(AppConsts.RETURN_TRUE);
        return response;
    }

    /*
    private SimpMessagingTemplate template;
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    LiveChatController(SimpMessagingTemplate template,SimpUserRegistry simpUserRegistry ){
        this.template = template;
        this.simpUserRegistry = simpUserRegistry;
    }

    @MessageMapping("/send/message")
    public void onReceivedMesage(String message){
        this.template.convertAndSend("/chat",  new SimpleDateFormat("HH:mm:ss").format(new Date())+"- "+message);
    }

    @MessageMapping("/{roomId}")  // send mapping always combind serviceid and user id  let's say serviceId+userId
    private void sendMessage2privateRoom(String message, @DestinationVariable String roomId) throws IOException {
        this.template.convertAndSend("/privateRoom/" + roomId,  new SimpleDateFormat("HH:mm:ss").format(new Date())+"- "+message);
    }

    @MessageMapping("/sendUser/{roomId}")  // send mapping serviceid as room and specify the user id  l
    private void sendMessage2user(String message, @DestinationVariable String roomId) throws IOException {
        SimpUser user =  simpUserRegistry.getUsers().stream().collect(Collectors.toList()).get(0);
        this.template.convertAndSendToUser(user.getName(), "/privateRoom/" + roomId,  new SimpleDateFormat("HH:mm:ss").format(new Date())+"- "+message);
        //this.template.convertAndSendtoUser( "","/privateRoom/",  new SimpleDateFormat("HH:mm:ss").format(new Date())+"- "+message);
    }
*/
    /*
    @MessageMapping("/{serviceId}/{userId}")  // send mapping always combind serviceid and user id this as a user 
    private void sendMessagetoVendor(String message, @DestinationVariable String serviceId) throws IOException {
        this.template.convertAndSend("/privateRoom/" + serviceId,  new SimpleDateFormat("HH:mm:ss").format(new Date())+"- "+message);
    }
    */
    
    /*
    @MessageMapping("/hello")  // sending message mapping
    @SendTo("/topic/greetings")  //subsribing topic  // for vendor should listion all clients  , for user should subsribed all service 
    public Greeting greeting(HelloMessage message) throws Exception {
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    */

}
