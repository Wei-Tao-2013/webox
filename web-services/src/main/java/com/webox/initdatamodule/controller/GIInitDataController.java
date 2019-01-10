package com.webox.initdatamodule.controller;

import com.webox.common.model.RepInitData;
import com.webox.common.model.ReqInitData;
import com.webox.common.model.Response;
import com.webox.common.utils.AppData;
import com.webox.initdatamodule.service.InitDataAPI;

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
public class GIInitDataController {
  
    @Autowired 
    private InitDataAPI initDataAPI;
    private static final Logger logger = LoggerFactory.getLogger(GIInitDataController.class);
  
    @RequestMapping(value="/wb/loadInitData",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody RepInitData loadInitData(@RequestBody ReqInitData request) throws Throwable  {		
      return initDataAPI.loadInitData(request);
    }


    @RequestMapping(value = "/wb/serviceFunctionStatus", method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody RepInitData LoadServiceFunctionStatus(@RequestBody ReqInitData request) throws Throwable  {
      RepInitData response = new RepInitData();
      response.setServiceFunction(AppData.serviceFunction);
		  return response;
    }

    @RequestMapping(value = "/wb/serviceFunctionStatus/{status}/{key}", method = RequestMethod.GET,headers="Accept=text/plain")
    public @ResponseBody String updateServiceFunctionStatus(@PathVariable("status") String status,@PathVariable("key") String key) throws Throwable  {
      if ("GIUookee".equals(key)){
        AppData.serviceFunction = status;
      }
      return  AppData.serviceFunction;
    }


}
