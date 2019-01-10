package com.webox.quote.controller;

import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.quote.service.QuoteAPI;
import com.webox.watchlistmodule.service.WatchListAPI;

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
public class GIQuoteController {
    private static final Logger logger = LoggerFactory.getLogger(GIQuoteController.class);
    @Autowired
    private QuoteAPI quoteAPI;
    /*
    @RequestMapping(value = "/wb/searchServices", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response searchServices(@RequestBody Request request) throws Throwable {
        return quoteAPI.searchServices(request);
    }
    */

    @RequestMapping(value = "/wb/postAJob", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response postAJob(@RequestBody Request request) throws Throwable {
        return quoteAPI.postAJob(request);
    }

    @RequestMapping(value = "/wb/postAJobAndAQuote", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response postAJobAndAQuote(@RequestBody Request request) throws Throwable {
        return quoteAPI.postAJobAndAQuote(request);
    }

    @RequestMapping(value = "/wb/loadAJob", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response loadAJob(@RequestBody Request request) throws Throwable {
        return quoteAPI.loadAJob(request);
    }

    @RequestMapping(value = "/wb/loadJobListByUser", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response loadJobListByUser(@RequestBody Request request) throws Throwable {
        return quoteAPI.loadJobListByUser(request);
    }

    @RequestMapping(value = "/wb/sendAQuote", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response sendAQuote(@RequestBody Request request) throws Throwable {
        return quoteAPI.sendAQuote(request);
    }

    @RequestMapping(value = "/wb/loadQuotesByService", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response loadQuotesByService(@RequestBody Request request) throws Throwable {
        return quoteAPI.loadQuotesByService(request);
    }

    @RequestMapping(value = "/wb/loadQuotesByUser", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response loadQuotesByUser(@RequestBody Request request) throws Throwable {
        return quoteAPI.loadQuotesByUser(request);
    }

    @RequestMapping(value = "/wb/loadQuotesByVendor", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response loadQuotesByVendor(@RequestBody Request request) throws Throwable {
        return quoteAPI.loadQuotesByVendor(request);
    }

    @RequestMapping(value = "/wb/loadQuotesByServiceAndUser", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response loadQuotesByServiceAndUser(@RequestBody Request request) throws Throwable {
        return quoteAPI.loadQuotesByServiceAndUser(request);
    }

    @RequestMapping(value = "/wb/quotePriceSent", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response quotePriceSent(@RequestBody Request request) throws Throwable {
        return quoteAPI.quotePriceSent(request);
    }

    @RequestMapping(value = "/wb/quoteAccept", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response quoteAccept(@RequestBody Request request) throws Throwable {
        return quoteAPI.quoteAccept(request);
    }

    @RequestMapping(value = "/wb/quoteRejected", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody Response quoteRejected(@RequestBody Request request) throws Throwable {
        return quoteAPI.quoteRejected(request);
    }

}
