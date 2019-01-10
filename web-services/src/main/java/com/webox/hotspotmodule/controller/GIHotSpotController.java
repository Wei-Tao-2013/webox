package com.webox.hotspotmodule.controller;
import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.hotspotmodule.service.HotSpotAPI;
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
public class GIHotSpotController {
    private static final Logger logger = LoggerFactory.getLogger(GIHotSpotController.class);
    @Autowired 
    private HotSpotAPI hotSpotAPI;
 
    @RequestMapping(value="/wb/toggleHotSpotList",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response toggleHotSpotList(@RequestBody Request request) throws Throwable  {		
        return hotSpotAPI.toggleHotSpotList(request);
    }

    @RequestMapping(value="/wb/loadHotSpotServices",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response  loadHotSpotService(@RequestBody Request request) throws Throwable  {		
        return hotSpotAPI.loadHotSpotServices(request);
    }


}
