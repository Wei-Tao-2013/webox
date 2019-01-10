package com.webox.order.controller;

import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.order.service.OrderAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class GIOrderController {
    private static final Logger logger = LoggerFactory.getLogger(GIOrderController.class);
    @Autowired
    private OrderAPI orderAPI;

    @RequestMapping(value = "/wb/completeAnOrder", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response completeAnOrder(@RequestBody Request request) throws Throwable {
        return orderAPI.completeAnOrder(request);
    }

    @RequestMapping(value = "/wb/cancelAnOrder", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response cancelAnOrder(@RequestBody Request request) throws Throwable {
        return orderAPI.cancelAnOrder(request);
    }

    @RequestMapping(value = "/wb/loadAnOrder", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response loadAnOrder(@RequestBody Request request) throws Throwable {
        return orderAPI.loadAnOrder(request);
    }

    @RequestMapping(value = "/wb/loadOrdersByService", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response loadOrdersByService(@RequestBody Request request) throws Throwable {
        return orderAPI.loadOrdersByService(request);
    }

    @RequestMapping(value = "/wb/loadOrdersByUser", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response loadOrdersByUser(@RequestBody Request request) throws Throwable {
        return orderAPI.loadOrdersByUser(request);
    }

    @RequestMapping(value = "/wb/loadOrdersByVendor", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response loadOrdersByVendor(@RequestBody Request request) throws Throwable {
        return orderAPI.loadOrdersByVendor(request);
    }

    @RequestMapping(value = "/wb/loadOrdersByServiceAndUser", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response loadOrdersByServiceAndUser(@RequestBody Request request) throws Throwable {
        return orderAPI.loadOrdersByServiceAndUser(request);
    }

}
