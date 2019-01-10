package com.webox.common.process;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.webox.common.model.Account;
import com.webox.common.model.Message;
import com.webox.common.model.MessageCount;
import com.webox.common.model.Service;
import com.webox.common.model.User;
import com.webox.common.repository.HotSpotRepository;
import com.webox.common.repository.MessageRepository;
import com.webox.common.repository.ServiceRepository;
import com.webox.common.repository.UserRepository;
import com.webox.common.utils.AppConsts;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;

@Component("messageManage")
public class MessageManage {
    private static final Logger logger = LoggerFactory.getLogger(MessageManage.class);

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    HotSpotRepository hotSpotRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ServiceRepository serviceRepository;

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    public void saveMessageList(List<Message> messageList) {
        messageRepository.saveAll(messageList);
    }

    public List<Message> loadMessagebyExample(Example<Message> example) {
        List<Message> messageList = messageRepository.findAll(example);
        return messageList;
    }

    public List<Message> retrieveMessagebyUserId(String userId) {
        Message message = new Message();
        message.setReceiver(new ObjectId(userId));
        message.setMsgStatus(0);
        message.setMsgChannel("service");
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("msgInQReason", "trytoSendRound");
        return this.retrieveMessage(message, matcher);
    }

    public List<Message> retrieveMessagebyUserIdandSenderId(String userId, String senderId) {
        Message message = new Message();
        message.setReceiver(new ObjectId(userId));
        message.setSender(new ObjectId(senderId));
        message.setMsgStatus(0);
        message.setMsgChannel("service");
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("msgInQReason", "trytoSendRound");
        return this.retrieveMessage(message, matcher);
    }

    public List<MessageCount> loadMessageAccount(String userId) {
        // for unread message account
        Message message = new Message();
        message.setReceiver(new ObjectId(userId));
        message.setMsgChannel("service");
        message.setMsgStatus(0);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("msgInQReason", "trytoSendRound");
        Example<Message> example = Example.of(message, matcher);
        List<Message> messageList = messageRepository.findAll(example);
        // get service account message
        // List<Service> hotSpotService =
        // hotSpotRepository.loadHotSpotServicesByUser(userId);
        // List<Service> hotSpotService =
        // serviceRepository.loadHotSpotServicesForUser(userId);
        // Map<String,List<Service>> hostSpotSrv =
        // hotSpotService.stream().collect(Collectors.groupingBy(Service::getUserId));

        Map<ObjectId, List<Message>> map = messageList.stream().collect(Collectors.groupingBy(Message::getSender));
        List<MessageCount> msgAccount = new ArrayList<MessageCount>();
        map.forEach((k, v) -> {
            List<Message> inComingList = v.stream().filter(o -> o.getMsgStatus() == 0).collect(Collectors.toList()); // new
                                                                                                                     // income
                                                                                                                     // message
            Optional<Message> msg = v.stream().max(Comparator.comparing(Message::getInComeTime));
            MessageCount msgCount = new MessageCount();

            if (msg.isPresent()) {
                msgCount.setLastMsg(msg.get().getMsgContent());
                msgCount.setLastMsgTime(msg.get().getInComeTime());
                msgCount.setTotal(inComingList.size());
                msgCount.setUserId(msg.get().getSender().toHexString());

                /* check if this user has approved service */
                /*
                 * List<Service> serviceList =
                 * serviceRepository.findByUserId(msgCount.getUserId());
                 * 
                 * if (serviceList.isEmpty()){ msgCount.setVendorInd(1); // 1 customer }else{
                 * List<Service> approvedServices =
                 * serviceList.stream().filter(o->("APPROVED").equalsIgnoreCase(o.getStatus())).
                 * collect(Collectors.toList()); if (approvedServices.isEmpty()){
                 * msgCount.setVendorInd(1); // 1 customer }else { msgCount.setVendorInd(0); //
                 * 0 vendor
                 * msgCount.setServiceType(AppData.serviceType.get(approvedServices.get(0).
                 * getServiceType()));
                 * msgCount.setServiceCity(AppData.cityCode.get(approvedServices.get(0).
                 * getAddress().getCity())); //msgCount.setService(approvedServices.get(0));
                 * msgCount.setServices(approvedServices); } }
                 */

                /* end if check */

                /*
                 * List<Service> hsService =
                 * hotSpotService.stream().filter(s->s.getUserId().equalsIgnoreCase(msgCount.
                 * getUserId())).collect(Collectors.toList());
                 * 
                 * if (hsService.isEmpty()){ msgCount.setVendorInd(1); // 1 customer } else {
                 * msgCount.setVendorInd(0); // 0 vendor
                 * msgCount.setServiceType(AppData.serviceType.get(hsService.get(0).
                 * getServiceType()));
                 * msgCount.setServiceCity(AppData.cityCode.get(hsService.get(0).getAddress().
                 * getCity())); msgCount.setService(hsService.get(0));
                 * 
                 * }
                 */
                // get avatar
                User user = userRepository.findByUserId(msg.get().getSender().toHexString());
                if (user != null) {
                    List<Account> account = user.getUserAccount().stream()
                            .filter(acc -> AppConsts.accountType.WECHAT.toString().equalsIgnoreCase(acc.getAccType()))
                            .collect(Collectors.toList());
                    if (!account.isEmpty()) {
                        msgCount.setAvatar(account.get(0).getAccAvatar());
                        msgCount.setUserName(account.get(0).getAccName());
                    }
                }
                // end
                msgAccount.add(msgCount);
            }
            ;
        });

        /*
         * Set<String> msgAccount1UserId = msgAccount.stream()
         * .map(MessageCount::getUserId).collect(Collectors.toSet()); // vendor list
         * process hostSpotSrv.forEach((k,v)->{ if (!msgAccount1UserId.contains(k)){
         * 
         * MessageCount msgCount = new MessageCount(); User user =
         * this.getVendorByServiceId(v.get(0).getServiceId()); if (user !=null) {
         * msgCount.setUserId(user.getUserId()); List<Account> account =
         * user.getUserAccount().stream().filter(acc ->
         * AppConsts.accountType.WECHAT.toString().equalsIgnoreCase(acc.getAccType())).
         * collect(Collectors.toList()); if (!account.isEmpty()){
         * msgCount.setAvatar(account.get(0).getAccAvatar());
         * msgCount.setUserName(account.get(0).getAccName()); } }
         * msgCount.setServices(v); msgCount.setVendorInd(0); // 0 vendor
         * msgCount.setServiceType(AppData.serviceType.get(v.get(0).getServiceType()));
         * if (v.get(0).getAddress() != null ) {
         * msgCount.setServiceCity(AppData.cityCode.get(v.get(0).getAddress().getCity())
         * ); } msgAccount.add(msgCount); }
         * 
         * });
         */

        // vendor list process
        /*
         * hotSpotService.forEach(s -> { if
         * (!msgAccount1UserId.contains(s.getUserId())){ MessageCount msgCount = new
         * MessageCount(); User user = this.getVendorByServiceId(s.getServiceId()); if
         * (user !=null) { msgCount.setUserId(user.getUserId()); List<Account> account =
         * user.getUserAccount().stream().filter(acc ->
         * AppConsts.accountType.WECHAT.toString().equalsIgnoreCase(acc.getAccType())).
         * collect(Collectors.toList()); if (!account.isEmpty()){
         * msgCount.setAvatar(account.get(0).getAccAvatar());
         * msgCount.setUserName(account.get(0).getAccName()); } }
         * msgCount.setService(s); msgCount.setVendorInd(0); // 0 vendor
         * msgCount.setServiceType(AppData.serviceType.get(s.getServiceType())); if
         * (s.getAddress() != null ) {
         * msgCount.setServiceCity(AppData.cityCode.get(s.getAddress().getCity())); }
         * msgAccount.add(msgCount); } });
         */

        return msgAccount;
    }

    public Message constructMessageObj(HashMap<String, String> map) {
        Message messageObj = new Message();
        // convert the to message object
        LocalDateTime localDateTime = LocalDateTime.now();
        messageObj.setMsgPayload(map.get("msgPayload"));
        messageObj.setMsgContent(map.get("msgContent"));
        messageObj.setInComeTime(localDateTime); // convert string to locatdate time
        messageObj.setMsgChannel("service");
        messageObj.setMsgSentIp(map.get("msgSentIp"));
        // messageObj.setMsgStatus(msgStatus);
        messageObj.setMsgType(map.get("msgType"));
        messageObj.setReceiver(new ObjectId(map.get("receiver")));
        messageObj.setSender(new ObjectId(map.get("sender")));
        messageObj.setSendOutTime(localDateTime);
        return messageObj;
    }

    public List<Message> constructMessageObjList(List<String> receiverIds, String senderId, String msgType,
            String msgContent) {
        List<Message> messageObjList = new ArrayList<Message>();

        if (receiverIds != null) {
            LocalDateTime localDateTime = LocalDateTime.now();
            Instant instant = Instant.now();
            String timeStampSeconds = String.valueOf(instant.getEpochSecond());
           // String messagePayload = this.contstructPayload(o, senderId, msgContent, timeStampSeconds, msgType)

            receiverIds.forEach(o -> {

                Message messageObj = new Message();
                // convert the to message object
                messageObj.setMsgPayload(this.contstructPayload(o, senderId, msgContent, timeStampSeconds, msgType));
                messageObj.setMsgContent(msgContent);
                messageObj.setInComeTime(localDateTime); // convert string to locatdate time
                messageObj.setMsgChannel("service");
                messageObj.setTrytoSendRound(0);
                messageObj.setTrytoSendRound(0);
                messageObj.setMsgStatus(0);// in Q
                messageObj.setMsgInQReason(2);// websocket disconnnected
                messageObj.setMsgType(msgType);
                messageObj.setReceiver(new ObjectId(o));
                messageObj.setSender(new ObjectId(senderId));
                messageObj.setSendOutTime(localDateTime);
                messageObjList.add(messageObj);
            });
        }
        return messageObjList;
    }

    public Message constructMessageObj(String receiverId, String senderId, String msgType, String msgContent) {
        Message messageObj = new Message();
        // convert the to message object
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = Instant.now();
        String timeStampSeconds = String.valueOf(instant.getEpochSecond());
        // convert the to message object
        messageObj.setMsgPayload(this.contstructPayload(receiverId, senderId, msgContent, timeStampSeconds, msgType));
        messageObj.setMsgContent(msgContent);
        messageObj.setInComeTime(localDateTime); // convert string to locatdate time
        messageObj.setMsgChannel("service");
        messageObj.setTrytoSendRound(0);
        messageObj.setTrytoSendRound(0);
        messageObj.setMsgStatus(0);// in Q
        messageObj.setMsgInQReason(2);// websocket disconnnected
        messageObj.setMsgType(msgType);
        messageObj.setReceiver(new ObjectId(receiverId));
        messageObj.setSender(new ObjectId(senderId));
        messageObj.setSendOutTime(localDateTime);
        return messageObj;
    }

    private String contstructPayload(String receiverId, String senderId, String content, String timeStamp,
            String msgType) {
        StringBuffer strbf = new StringBuffer("{");
        strbf.append("\"receiver\":\"").append(receiverId).append("\",").append("\"msgContent\":\"").append(content)
                .append("\",").append("\"msgType\":\"").append(msgType).append("\",").append("\"sender\":\"")
                .append(senderId).append("\",").append("\"inComeTime\":\"").append(timeStamp).append("\"}");

        return strbf.toString();

    }

    public List<MessageCount> loadUnreadMsgCount(String receiver) {
        return messageRepository.unReadMessageCount(receiver);
    }

    private User getVendorByServiceId(String serviceId) {
        Service service = serviceRepository.findByServiceId(serviceId);
        User user = userRepository.findByUserId(service.getUserId());
        return user;
    }

    private List<Message> retrieveMessage(Message message, ExampleMatcher matcher) {
        Example<Message> example = Example.of(message, matcher);
        List<Message> messageList = messageRepository.findAll(example);
        messageList.sort((a, b) -> {
            if (a.getInComeTime().isAfter(b.getInComeTime()))
                return 1;
            else
                return 0;
        });
        return messageList;
    }

}