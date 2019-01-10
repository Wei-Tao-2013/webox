package com.webox.order.process;

import com.webox.common.model.AppMessage;
import com.webox.common.model.Order;
import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.process.OrderManage;
import com.webox.common.process.ServiceManage;
import com.webox.common.utils.AppConsts;
import com.webox.order.service.OrderAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OrderImpl implements OrderAPI {

	private static final Logger logger = LoggerFactory.getLogger(OrderImpl.class);

	@Autowired
	@Qualifier("orderManage")
	private OrderManage orderManage;

	@Autowired
	@Qualifier("generalServiceManage")

	private ServiceManage serviceManage;
	private AppConsts appConts = new AppConsts();
	private AppMessage appMessage = new AppMessage();

	@Override
	public Response completeAnOrder(Request request) {
		Response response = new Response();
		this.appMessage = orderManage.completeAnOrder(request.getOrderId());
		response.setAppMessage(this.appMessage);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response loadAnOrder(Request request) {
		Response response = new Response();
		Order order = orderManage.loadAnOrder(request.getOrderId());
		if (order != null) {
			response.setOrder(order);
			this.appMessage.setDataReturn(request.getOrderId());
			this.appMessage.setMessage("ORDER_LOADED [ORDER ID]");
			this.appMessage.setMsgType("OK");
			this.appMessage.setMsgId("order-03");
			response.setOrderId(order.getOrderId());
		} else {
			this.appMessage.setDataReturn(request.getOrderId());
			this.appMessage.setMessage("ORDER_NOT_FOUND [ORDER ID]");
			this.appMessage.setMsgType("ERR");
			this.appMessage.setMsgId("order-err-03");
		}
		response.setAppMessage(this.appMessage);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response loadOrdersByService(Request request) {
		Response response = new Response();
		response.setOrderList(orderManage.loadOrdersByService(request.getServiceId()));
		this.appMessage.setDataReturn(String.valueOf(response.getOrderList().size()));
		this.appMessage.setMessage("ORDERS_LOADED [ORDERS SIZE]");
		this.appMessage.setMsgType("OK");
		this.appMessage.setMsgId("order-04");
		response.setAppMessage(this.appMessage);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response loadOrdersByUser(Request request) {
		Response response = new Response();
		response.setOrderGroup(orderManage.loadOrdersByUser(request.getUserId()));
		this.appMessage.setDataReturn(String.valueOf(response.getOrderGroup().size()));
		this.appMessage.setMessage("ORDERS_LOADED [ORDERS SIZE]");
		this.appMessage.setMsgType("OK");
		this.appMessage.setMsgId("order-04");
		response.setAppMessage(this.appMessage);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response loadOrdersByServiceAndUser(Request request) {
		Response response = new Response();
		response.setOrderList(orderManage.loadOrdersByServiceAndUser(request.getServiceId(), request.getUserId()));
		this.appMessage.setDataReturn(String.valueOf(response.getOrderList().size()));
		this.appMessage.setMessage("ORDERS_LOADED [ORDER SIZE]");
		this.appMessage.setMsgType("OK");
		this.appMessage.setMsgId("order-04");
		response.setAppMessage(this.appMessage);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response loadOrdersByVendor(Request request) {
		Response response = new Response();
		response.setOrderGroup(orderManage.loadOrdersByVendor(request.getVendorId()));
		this.appMessage.setDataReturn(String.valueOf(response.getOrderGroup().size()));
		this.appMessage.setMessage("ORDERS_LOADED [ORDER SIZE]");
		this.appMessage.setMsgType("OK");
		this.appMessage.setMsgId("order-04");
		response.setAppMessage(this.appMessage);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response cancelAnOrder(Request request) {
		Response response = new Response();
		this.appMessage = orderManage.cancelAnOrder(request.getOrderId());
		response.setAppMessage(this.appMessage);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

}