package com.webox.hotspotmodule.service;

import com.webox.common.model.Request;
import com.webox.common.model.Response;

public interface HotSpotAPI{

    Response toggleHotSpotList(Request request);
    Response loadHotSpotServices(Request request);
   
}