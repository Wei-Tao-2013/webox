package com.webox.servicemodule.process; 

import com.webox.common.model.FileData;
import com.webox.common.model.Photo;
import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.model.Service;
import com.webox.common.process.FileStore;
import com.webox.common.process.ServiceManage;
import com.webox.common.utils.AppConsts;
import com.webox.common.utils.GIutils;
import com.webox.exception.WBException;
import com.webox.servicemodule.service.FileStorageAPI;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;

import org.bson.types.Binary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

@Component
public class FileStorageImpl implements FileStorageAPI {
 
    @Autowired @Qualifier("generalServiceManage")
    private ServiceManage serviceManage;

    @Autowired @Qualifier("fileStore")
    private FileStore fileStore;
   

    public Response storeFile(String requestStr,MultipartFile file) throws WBException {
       
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Response response = new Response();
        try {
            Binary data = new Binary(file.getBytes());
            Request request = new Request(); 
            Service service = new Gson().fromJson(requestStr, Service.class);
            request.setService(service);
            response = serviceManage.storeImage(request, fileName, data);
            return response;
        } catch (IOException ex) {
            throw new WBException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Response loadFileAsResource(String serviceId) throws WBException {
            List<Photo> photoList = serviceManage.getImages(serviceId).getPhotoList();
            List<String> photoIdList = new ArrayList<String>();
            photoList.forEach(o->{
                photoIdList.add(o.getFileId());
            });

        Response response = new Response();
        response.setAppStatus(AppConsts.RETURN_TRUE);
        response.setFileIdList(photoIdList);
        return response;
        
    }

	@Override
	public FileData getFile(String fileId) {
		return fileStore.getFile(fileId);
    }

	@Override
	public Response deleteFile(String fileId) {
        
        serviceManage.deleteImage(fileId); // remove the connenction to filedata

		return fileStore.deleteFile(fileId);
	}
    
}