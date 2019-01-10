package com.webox.mediadata.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.exception.WBException;
import com.webox.mediadata.service.DataStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

@Controller
@CrossOrigin
public class GIServiceDataController {
    private static final Logger logger = LoggerFactory.getLogger(GIServiceDataController.class);
   
    @Autowired @Qualifier("serviceFileStorage")
    private DataStorage serviceFileStorage;
   
    @RequestMapping(value="/wb/service/uploadFile",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("sourceData") String sourceData) {		
        Response response = new Response();
        try {
            response =serviceFileStorage.storeFile(sourceData,file);
             return response;
		} catch (WBException e) {
            e.printStackTrace();
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            response.setAppStatus("false");
            response.setAppInfo(fileName + "uploaded failed ");
            return response;
		}
    }

    @RequestMapping(value="/wb/service/uploadMultipleFiles",method = RequestMethod.POST,headers="Accept=application/json")
    public List<Response> uploadMultipleServiceFiles(@RequestParam("files") MultipartFile[] files,@RequestParam("sourceData") String sourceData) throws Throwable {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file,sourceData))
                .collect(Collectors.toList());
    }

    @RequestMapping(value="/wb/service/loadPhotos",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response loadServicePhotos(@RequestBody Request request) throws Throwable  {	
        String resourceId = request.getServiceId();
        return serviceFileStorage.loadFileAsResource(resourceId);
    }

    @RequestMapping(value="/wb/service/deleteFile",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response deleteServiceFile(@RequestBody Request request) throws Throwable  {	
        String fileId = request.getFileId();
        return serviceFileStorage.deleteFile(fileId);
    }
    
}
