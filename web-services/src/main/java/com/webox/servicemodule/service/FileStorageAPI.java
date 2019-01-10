package com.webox.servicemodule.service;

import java.util.Map;

import com.webox.common.model.FileData;
import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.exception.WBException;

import org.bson.types.Binary;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageAPI{

    Response storeFile(String request,MultipartFile file) throws WBException;
    
    Response loadFileAsResource(String serviceId) throws WBException;

    FileData getFile(String fileId);

    Response deleteFile(String fileId);
    
   
}