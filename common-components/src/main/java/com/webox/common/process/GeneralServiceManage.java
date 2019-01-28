package com.webox.common.process;

import java.time.LocalDateTime;
import java.util.List;

import com.webox.common.model.Photo;
import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.model.Service;
import com.webox.common.utils.AppConsts;

import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("generalServiceManage")
public class GeneralServiceManage extends ServiceManage {

    private static final Logger logger = LoggerFactory.getLogger(GeneralServiceManage.class);

    public GeneralServiceManage() {

    }

    @Override
    public Response storeImage(Request request, String fileName, Binary data) {
        Service service = new Service();
        service = request.getService();
        String serviceId;
        Response response = new Response();
        LocalDateTime localDateTime = LocalDateTime.now();

        String fileId = fileStore.storeFile(data);
        if (fileName == null || "".equalsIgnoreCase(fileName.trim())) {
            fileName = fileId;
        }
        Photo photo = new Photo();
        photo.setFileId(fileId);
        photo.setPhotoName(fileName);
        photo.setStatus("available");
        photo.setUpdateTime(localDateTime);
        photo.setUploadTime(localDateTime);

        Service existService = serviceRepository.findByServiceId(service.getServiceId());
        if (existService != null) {
            existService.addServicePhoto(photo);
            serviceRepository.save(existService);
            serviceId = existService.getServiceId();
        } else {
            service.addServicePhoto(photo);
            serviceRepository.save(service);
            serviceId = service.getServiceId();
        }

        response.setFileId(fileId);
        response.setServiceId(serviceId);
        response.setAppStatus(AppConsts.RETURN_TRUE);
        response.setAppInfo(fileName + " is stored");
        return response;
    }

    @Override
    public Response getImages(String serviceId) {
        Service service = serviceRepository.findByServiceId(serviceId);
        Response response = new Response();
        if (service != null) {
            List<Photo> photoList = service.getServicePhoto();
            response.setPhotoList(photoList);
            response.setAppStatus(AppConsts.RETURN_TRUE);
        } else {
            response.setPhotoList(null);
            response.setAppStatus(AppConsts.RETURN_TRUE);
        }
        return response;
    }

    public void deleteImage(String fileId) {
        serviceRepository.deleteNestedPhoto(fileId);
    }

}