package com.webox.servicemodule.service;

import com.webox.common.model.Request;
import com.webox.common.model.Response;

public interface ServiceAPI{

    Response draftAService(Request request);
    Response registerAService(Request request);
    Response loadServices(String userId,String serviceStatus);
    Response loadAService(String serviceId);
    Response loadAService(String serviceId,String userId);
    Response searchingService(String searchingWords);
    
   
}