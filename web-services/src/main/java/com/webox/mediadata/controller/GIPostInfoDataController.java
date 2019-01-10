package com.webox.mediadata.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.webox.common.model.FileData;
import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.exception.WBException;
import com.webox.mediadata.service.DataStorage;

import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class GIPostInfoDataController {
    private static final Logger logger = LoggerFactory.getLogger(GIPostInfoDataController.class);
    
    @Autowired @Qualifier("postInfoFileStorage")
    private DataStorage postInfoFileStorage;

    @RequestMapping(value="/wb/post/uploadFile",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("sourceData") String sourceData) {		
        Response response = new Response();
        try {
            response =postInfoFileStorage.storeFile(sourceData,file);
             return response;
		} catch (WBException e) {
            e.printStackTrace();
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            response.setAppStatus("false");
            response.setAppInfo(fileName + "uploaded failed ");
            return response;
		}
    }

    @RequestMapping(value="/wb/post/uploadMultipleFiles",method = RequestMethod.POST,headers="Accept=application/json")
    public List<Response> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,@RequestParam("sourceData") String sourceData) throws Throwable {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file,sourceData))
                .collect(Collectors.toList());
    }

    @RequestMapping(value="/wb/post/loadPhotos",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response loadPhotos(@RequestBody Request request) throws Throwable  {	
        String resourceId = request.getPostInfoId();
        return postInfoFileStorage.loadFileAsResource(resourceId);
    }

    @RequestMapping(value = "/wb/file/{fileId}", method = RequestMethod.GET,headers="Accept=text/plain",
    produces = MediaType.IMAGE_JPEG_VALUE)
        public ResponseEntity<byte[]> getFile(@PathVariable("fileId") String fileId)  {
        Binary data = null;
        FileData fileData = postInfoFileStorage.getFile(fileId);
        data = fileData.getFileData();
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(data.getData());
        }

    @RequestMapping(value="/wb/post/deleteFile",method = RequestMethod.POST,headers="Accept=application/json")
    public @ResponseBody Response deleteFile(@RequestBody Request request) throws Throwable  {	
        String fileId = request.getFileId();
        return postInfoFileStorage.deleteFile(fileId);
    }
 
}
