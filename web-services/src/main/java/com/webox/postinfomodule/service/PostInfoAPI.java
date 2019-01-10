package com.webox.postinfomodule.service;

import com.webox.common.model.Request;
import com.webox.common.model.Response;

public interface PostInfoAPI{

    Response draftAPostInfo(Request request);
    Response deleteAPostInfo(Request request);
    Response publishAPostInfo(Request request);
    Response loadPostInfos(String serviceId,String postInfoStatus);
    Response loadAPostInfo(String postInfoId);
   
}