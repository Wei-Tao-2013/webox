package com.webox.postinfomodule.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.webox.common.model.FileData;
import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.exception.WBException;
import com.webox.mediadata.service.DataStorage;
import com.webox.postinfomodule.service.PostInfoAPI;

import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@CrossOrigin
public class GIPostInfoController {
    private static final Logger logger = LoggerFactory.getLogger(GIPostInfoController.class);
    
    @Autowired 
    private PostInfoAPI postInfoAPI;

    @Autowired @Qualifier("postInfoFileStorage")
    private DataStorage dataStorageAPI;
    
    @RequestMapping(value="/wb/draftAPostInfo",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response draftAPostInfo(@RequestBody Request request) throws Throwable  {		
        return postInfoAPI.draftAPostInfo(request);
    }

    @RequestMapping(value="/wb/publishAPostInfo",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response publishAPostInfo(@RequestBody Request request) throws Throwable  {		
        return postInfoAPI.publishAPostInfo(request);
    }

    @RequestMapping(value="/wb/loadPostInfos",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response loadPostInfos(@RequestBody Request request) throws Throwable  {		
        return postInfoAPI.loadPostInfos(request.getServiceId(),request.getPostStatus());
    }

    @RequestMapping(value="/wb/loadAPostInfo",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response loadAPostInfo(@RequestBody Request request) throws Throwable  {		
        return postInfoAPI.loadAPostInfo(request.getPostInfoId());
    }

    @RequestMapping(value="/wb/deleteAPostInfo",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response deleteAPostInfo(@RequestBody Request request) throws Throwable  {		
        return postInfoAPI.deleteAPostInfo(request);
    }
    
}
