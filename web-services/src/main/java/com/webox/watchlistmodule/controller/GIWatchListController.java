package com.webox.watchlistmodule.controller;
import com.webox.common.model.Request;
import com.webox.common.model.Response;
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
public class GIWatchListController {
    private static final Logger logger = LoggerFactory.getLogger(GIWatchListController.class);
    @Autowired 
    private WatchListAPI watchListAPI;
 
    @RequestMapping(value="/wb/toggleWatchList",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response toggleHotSpotList(@RequestBody Request request) throws Throwable  {		
        return watchListAPI.toggleWatchList(request);
    }

    @RequestMapping(value="/wb/loadWatchListServices",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response  loadHotSpotService(@RequestBody Request request) throws Throwable  {		
        return watchListAPI.loadWatchListServices(request);
    }

    @RequestMapping(value="/wb/loadWatchListWithPostBriefs",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response  loadWatchListWithPostBriefs(@RequestBody Request request) throws Throwable  {		
        return watchListAPI.loadWatchListServiceWithPostBriefList(request);
    }


}
