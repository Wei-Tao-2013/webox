package com.webox.common.process;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.webox.common.model.AppMessage;
import com.webox.common.model.Message;
import com.webox.common.model.Order;
import com.webox.common.model.Quote;
import com.webox.common.repository.OrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("orderManage")
public class OrderManage {
  private static final Logger logger = LoggerFactory.getLogger(OrderManage.class);

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  @Qualifier("messageManage")
  MessageManage messageManage;

  // private LocalDateTime localDateTime = LocalDateTime.now();
  private AppMessage appMessage = new AppMessage();

  // 0, Booked, 1 Completed , 2, Cancel
  public AppMessage bookAnOrder(Quote quote) {
    Order order = new Order();
    LocalDateTime localDateTime = LocalDateTime.now();
    order.setOrderBookedTime(localDateTime);
    order.setStatus(0);
    order.setQuoteId(quote.getQuoteId());
    order.setJobId(quote.getJobId());
    order.setUserId(quote.getUserId());
    order.setServiceId(quote.getServiceId());
    order.setServiceType(quote.getServiceType());
    order.setVendorId(quote.getVendorId());
    orderRepository.save(order);
    // send message to user

    Message msg = messageManage.constructMessageObj(order.getUserId(), order.getVendorId(), "text",
        "尊敬的用户， 您的服务已经预定成功!");
    msg.setMsgStatus(0);// in Q
    msg.setMsgInQReason(2);// websocket disconnnected
    msg.setSendOutTime(null); // yet to be sent
    msg.setTrytoSendRound(0);
    messageManage.saveMessage(msg);
    // System.out.println("message is in Q for receiver as websocket disconnected "
    // + receiver + " with message " + message);

    this.appMessage.setDataReturn(order.getOrderId());
    this.appMessage.setMessage("ORDER_BOOKED [ORDER ID]");
    this.appMessage.setMsgType("OK");
    this.appMessage.setMsgId("order-01");
    return this.appMessage;
  }

  public AppMessage cancelAnOrder(String orderId) {
    Order existingOrder = orderRepository.findByOrderId(orderId);
    LocalDateTime localDateTime = LocalDateTime.now();
    if (existingOrder != null) {
      if (this.orderOperationCheck(existingOrder, "cancelAnOrder")) {
        existingOrder.setStatus(2);
        existingOrder.setOrderCancelTime(localDateTime);
        orderRepository.save(existingOrder);
        this.appMessage.setDataReturn(existingOrder.getOrderId());
        this.appMessage.setMessage("ORDER_CANCELED [ORDER ID]");
        this.appMessage.setMsgType("OK");
        this.appMessage.setMsgId("order-02");
      } else {
        // nothing need to be do here
      }

    }
    return this.appMessage;
  }

  public List<String> loadUserIdListByServiceOrder(String serviceId){
    List<String> userIds = new ArrayList<String>();
    List<Order> orderLists = orderRepository.loadOrdersByService(serviceId);
    orderLists.forEach(o->{
        userIds.add(o.getUserId());
    });
    return userIds;
}


  public AppMessage completeAnOrder(String orderId) {
    Order existingOrder = orderRepository.findByOrderId(orderId);
    LocalDateTime localDateTime = LocalDateTime.now();
    if (existingOrder != null) {
      if (this.orderOperationCheck(existingOrder, "completeAnOrder")) {
        existingOrder.setStatus(1);
        existingOrder.setOrderCompletedTime(localDateTime);
        orderRepository.save(existingOrder);
        this.appMessage.setDataReturn(existingOrder.getOrderId());
        this.appMessage.setMessage("ORDER_COMPLETED [ORDER ID]");
        this.appMessage.setMsgType("OK");
        this.appMessage.setMsgId("order-03");
      } else {
        // nothing need to be do here
      }

    } else {
      this.appMessage.setDataReturn(orderId);
      this.appMessage.setMessage("ORDER_NOT_FOUND [ORDER ID]");
      this.appMessage.setMsgType("ERR");
      this.appMessage.setMsgId("order-err-03");
    }
    return this.appMessage;
  }

  public Order loadAnOrder(String orderId) {
    return orderRepository.findByOrderId(orderId);
  }

  public List<Order> loadOrdersByService(String serviceId) {
    return orderRepository.loadOrdersByService(serviceId);
  }

  public List<Order> loadOrdersByServiceAndUser(String serviceId, String userId) {
    return orderRepository.loadOrdersByServiceAndUser(serviceId, userId);
  }

  public Map<String, List<Order>> loadOrdersByUser(String userId) { // for customer view
    List<Order> orderList = orderRepository.loadOrdersByCustomer(userId);
    Map<String, List<Order>> map = orderList.stream().collect(Collectors.groupingBy(Order::getServiceId));
    return map;
  }

  public Map<String, List<Order>> loadOrdersByVendor(String vendorId) { // for vendor view
    List<Order> orderList = orderRepository.loadOrdersByVendor(vendorId);
    Map<String, List<Order>> map = orderList.stream().collect(Collectors.groupingBy(Order::getServiceId));
    return map;
  }

  // 0, Booked, 1 Completed , 2, Cancel
  private boolean orderOperationCheck(Order order, String operation) {
    boolean permission = true;
    switch (operation) {
    case "cancelAnOrder":
      if (order.getStatus() == 1) { // order is either done or cancel can't be cancel
        this.appMessage.setMsgId("order_err_01");
        this.appMessage.setMessage("order is completed already. [ORDER STATUS] ");
        this.appMessage.setDataReturn(order.getStatus() + "");
        this.appMessage.setMsgType("ERR");
        permission = false;
      } else if (order.getStatus() == 2) {
        this.appMessage.setMsgId("order_err_02");
        this.appMessage.setMessage("The order is canceled already. [ORDER STATUS]  ");
        this.appMessage.setDataReturn(order.getStatus() + "");
        this.appMessage.setMsgType("ERR");
        permission = false;
      }
      break;
    case "completeAnOrder":
      if (order.getStatus() == 1) { // order is either done or cancel can't be cancel
        this.appMessage.setMsgId("order_err_01");
        this.appMessage.setMessage("The order is completed already, [ORDER STATUS] ");
        this.appMessage.setDataReturn(order.getStatus() + "");
        this.appMessage.setMsgType("ERR");
        permission = false;
      } else if (order.getStatus() == 2) {
        this.appMessage.setMsgId("order_err_02");
        this.appMessage.setMessage("The order is canceled already, [ORDER STATUS]");
        this.appMessage.setDataReturn(order.getStatus() + "");
        this.appMessage.setMsgType("ERR");
        permission = false;
      }
      break;
    }
    return permission;
  }

}