package com.webox.mediadata.service;

import com.webox.common.model.FileData;
import com.webox.common.model.Response;
import com.webox.common.process.FileStore;
import com.webox.exception.WBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.multipart.MultipartFile;

public abstract class DataStorage{

    @Autowired @Qualifier("fileStore")
    private FileStore fileStore;
    
    // save data file
    public abstract Response storeFile(String request,MultipartFile file) throws WBException;
    //load data file id in list by bussiness resource
    public abstract Response loadFileAsResource(String resourceId) throws WBException;
    // generate link to access data
    public FileData getFile(String fileId) {
        return fileStore.getFile(fileId);
    }
    // delete file physically 
    public Response deleteFile(String fileId){
        return fileStore.deleteFile(fileId);
    }
    
   
}