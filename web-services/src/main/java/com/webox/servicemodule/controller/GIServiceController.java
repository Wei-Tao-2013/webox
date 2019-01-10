package com.webox.servicemodule.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.webox.common.model.FileData;
import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.exception.WBException;
import com.webox.servicemodule.service.FileStorageAPI;
import com.webox.servicemodule.service.ServiceAPI;

import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

@Controller
@CrossOrigin
public class GIServiceController {
    private static final Logger logger = LoggerFactory.getLogger(GIServiceController.class);
    @Autowired 
    private ServiceAPI serviceAPI;

    @Autowired
    private FileStorageAPI fileStorageService;

    @RequestMapping(value = "/wb/search/services/{searchingWords}", method = RequestMethod.GET,headers="Accept=text/plain")
     public @ResponseBody Response searchServices(@PathVariable("searchingWords") String searchingWords) throws Throwable {
        return serviceAPI.searchingService(searchingWords);
    }

    
    @RequestMapping(value="/wb/draftAService",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response draftAService(@RequestBody Request request) throws Throwable  {		
        return serviceAPI.draftAService(request);
    }

    @RequestMapping(value="/wb/registerAService",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response registerAService(@RequestBody Request request) throws Throwable  {		
        return serviceAPI.registerAService(request);
    }

    @RequestMapping(value="/wb/loadServices",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response loadServices(@RequestBody Request request) throws Throwable  {		
        return serviceAPI.loadServices(request.getUserId(),request.getServiceStatus());
    }

    @RequestMapping(value="/wb/loadAService",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response loadAService(@RequestBody Request request) throws Throwable  {		
        return serviceAPI.loadAService(request.getServiceId());
    }

    @RequestMapping(value="/wb/loadAServiceIfWatched",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response loadAServiceIfWatched(@RequestBody Request request) throws Throwable  {		
        return serviceAPI.loadAService(request.getServiceId(),request.getUserId());
    }

    @RequestMapping(value="/wb/uploadFile",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("serviceData") String serviceData) {		
        Response response = new Response();
        try {
            response =fileStorageService.storeFile(serviceData,file);
             return response;
		} catch (WBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            response.setAppStatus("false");
            response.setAppInfo(fileName + "uploaded failed");
            return response;
			
		}
    }

    @RequestMapping(value="/wb/uploadMultipleFiles",method = RequestMethod.POST,headers="Accept=application/json")
    public List<Response> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,@RequestParam("serviceData") String serviceData) throws Throwable {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file,serviceData))
                .collect(Collectors.toList());
    }

    @RequestMapping(value="/wb/loadPhotos",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response loadPhotos(@RequestBody Request request) throws Throwable  {	
        String serviceId = request.getServiceId();
        return fileStorageService.loadFileAsResource(serviceId);
    }

    @RequestMapping(value = "/wb/service/file/{fileId}", method = RequestMethod.GET,headers="Accept=text/plain",
    produces = MediaType.IMAGE_JPEG_VALUE)
        public ResponseEntity<byte[]> getFile(@PathVariable("fileId") String fileId)  {
        Binary data = null;
        FileData fileData =  fileStorageService.getFile(fileId);
        data = fileData.getFileData();
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(data.getData());
        }

    @RequestMapping(value="/wb/deleteFile",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response deleteFile(@RequestBody Request request) throws Throwable  {	
        String fileId = request.getFileId();
        return fileStorageService.deleteFile(fileId);
    }
    
}
