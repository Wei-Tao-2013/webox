package com.webox.order.service;

import com.webox.common.model.Request;
import com.webox.common.model.Response;

public interface OrderAPI {

	Response completeAnOrder(Request request); // by customer

	Response cancelAnOrder(Request request); // by vendor or customer

	Response loadAnOrder(Request request);

	Response loadOrdersByService(Request request); // by service id for vendor

	Response loadOrdersByUser(Request request); // for costomer

	Response loadOrdersByVendor(Request request); // for vendor view

	Response loadOrdersByServiceAndUser(Request request); // for costomer view

}