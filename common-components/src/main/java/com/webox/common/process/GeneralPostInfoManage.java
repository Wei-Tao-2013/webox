package com.webox.common.process;

import java.time.LocalDateTime;
import java.util.List;
import com.webox.common.model.Photo;
import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.model.PostInfo;
import com.webox.common.utils.AppConsts;

import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("generalPostInfoManage")
public class GeneralPostInfoManage extends PostInfoManage{
    
    private static final Logger logger = LoggerFactory.getLogger(GeneralPostInfoManage.class);
    
	@Override
	public Response storeImage(Request request,String fileName, Binary data) {
        PostInfo postInfo = new PostInfo();
        postInfo = request.getPostInfo();
        String postInfoId ;
        Response response = new Response();
        LocalDateTime localDateTime = LocalDateTime.now();
       
        String fileId = fileStore.storeFile(data);
        if (fileName == null || "".equalsIgnoreCase(fileName.trim())){
            fileName = fileId;
        }
        Photo photo = new Photo();
        photo.setFileId(fileId);
        photo.setPhotoName(fileName);
        photo.setStatus("available");
        photo.setUpdateTime(localDateTime);
        photo.setUploadTime(localDateTime);

        PostInfo existPostInfo = postInfoRepository.findByPostInfoId(postInfo.getPostInfoId());
        if (existPostInfo !=null) {
            existPostInfo.addPostPhoto(photo);
            postInfoRepository.save(existPostInfo);    
            postInfoId = existPostInfo.getPostInfoId();
        }else{
            postInfo.addPostPhoto(photo);
            postInfoRepository.save(postInfo);
            postInfoId= postInfo.getPostInfoId();
        }
        response.setFileId(fileId);
        response.setPostInfoId(postInfoId);
        response.setAppStatus(AppConsts.RETURN_TRUE);
        response.setAppInfo(fileName + " is stored");
        return response;
    }

	@Override
	public Response getImages(String postInfoId) {
        PostInfo postInfo =  postInfoRepository.findByPostInfoId(postInfoId);
        Response response = new Response();
        if (postInfo != null){
            List<Photo> photoList = postInfo.getPostPhoto();
            response.setPhotoList(photoList);
            response.setAppStatus(AppConsts.RETURN_TRUE);
        }else{
            response.setPhotoList(null);
            response.setAppStatus(AppConsts.RETURN_TRUE);
        }
        return response;
    }

    public void deleteImage(String fileId) {
        postInfoRepository.deleteNestedPhoto(fileId);
    }

}