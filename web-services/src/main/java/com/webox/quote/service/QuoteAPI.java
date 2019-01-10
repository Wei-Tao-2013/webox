package com.webox.quote.service;

import com.webox.common.model.Request;
import com.webox.common.model.Response;

public interface QuoteAPI {

   // Response searchServices(Request request);

    Response postAJob(Request request); // by job object
    
    Response postAJobAndAQuote(Request request); // by job object and quote

    Response loadAJob(Request request); // by jobid

    Response loadJobListByUser(Request request); // by userid

    Response sendAQuote(Request request);    // by job and service 

    Response loadQuotesByService(Request request); // by service id for vendor 

    Response loadQuotesByUser(Request request); // for costomer

    Response loadQuotesByVendor(Request request); // for vendor view

    Response loadQuotesByServiceAndUser(Request request); // for costmer view

    Response  quotePriceSent(Request request); // for vendor sending price

    Response  quoteAccept(Request request); // action by customer

    Response  quoteRejected(Request request); // by customer or vendor befor quote experied 

}