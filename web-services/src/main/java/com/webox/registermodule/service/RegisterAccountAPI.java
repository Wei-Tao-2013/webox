package com.webox.registermodule.service;

import com.webox.common.model.Request;
import com.webox.common.model.Response;

public interface RegisterAccountAPI{

    Response weixinRegister(Request request);
    Response weixinSignAccount(Request request);
    Response weixinSignOut(Request request);
    Response weixinProfileUpdate(Request request);
    Response refreshToken(Request request);
    Response loadWeiXinInfo(Request request);
   
}