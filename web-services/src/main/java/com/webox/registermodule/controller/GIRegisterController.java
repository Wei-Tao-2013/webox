package com.webox.registermodule.controller;

import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.utils.GIutils;
import com.webox.registermodule.service.RegisterAccountAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class GIRegisterController {
  
    @Autowired 
    private RegisterAccountAPI registerAPI;
    private static final Logger logger = LoggerFactory.getLogger(GIRegisterController.class);

    @RequestMapping(value = "/sayHello/{callName}/{message}", method = RequestMethod.GET,headers="Accept=text/plain")
    public @ResponseBody String sayHello(@PathVariable("callName") String callName,@PathVariable("message") String message) throws Throwable  {
		  return "hello bro "+ callName+" ---> "  +message;  
    }

    @RequestMapping(value = "/getMessageInfo/{callName}/{message}", method = RequestMethod.GET,headers="Accept=text/plain")
    public @ResponseBody Response getMessageInfo(@PathVariable("callName") String callName,@PathVariable("message") String message) throws Throwable  {
      Response response = new Response();
      response.setAppInfo(callName);
      response.setAppCode(message);
      return response;
    }

    @RequestMapping(value="/wb/weixinRegister",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response register(@RequestBody Request request) throws Throwable  {		
         return registerAPI.weixinRegister(request);
    }

    @RequestMapping(value="/weixinSignAccount",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response signAccount(@RequestBody Request request) throws Throwable  {		
         return registerAPI.weixinSignAccount(request);
    }

    @RequestMapping(value="/wb/weixinSignOut",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response signOutTest(@RequestBody Request request) throws Throwable  {		
         return registerAPI.weixinSignOut(request);
    }

    @RequestMapping(value="/wb/weixinProfileUpdate",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response weixinProfileUpdate(@RequestBody Request request) throws Throwable  {		
      logger.debug("controrll request is {}", GIutils.converToJson(request));
      return registerAPI.weixinProfileUpdate(request);
    }

    @RequestMapping(value="/wb/refreshToken",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response refreshToken(@RequestBody Request request) throws Throwable  {		
         return registerAPI.refreshToken(request);
    }

    @RequestMapping(value="/wb/loadWeiXinUserInfo",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response loadWeiXinUserInfo(@RequestBody Request request) throws Throwable  {		
         return registerAPI.loadWeiXinInfo(request);
    }
    

}
