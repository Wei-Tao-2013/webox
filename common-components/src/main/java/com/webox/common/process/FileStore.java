package com.webox.common.process;

import java.time.LocalDateTime;

import com.webox.common.model.FileData;
import com.webox.common.model.Response;
import com.webox.common.repository.FileDataRepository;
import com.webox.common.utils.AppConsts;

import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("fileStore")
public class FileStore {
    private static final Logger logger = LoggerFactory.getLogger(FileStore.class);

    @Autowired
    FileDataRepository fileDataRepository;

    public FileData getFile(String fileId) {
        FileData fileData = fileDataRepository.findByFileId(fileId);
        return fileData;
    }

    public String storeFile(Binary data) {
        FileData fileData = new FileData();
        LocalDateTime localDateTime = LocalDateTime.now();
        fileData.setFileData(data);
        fileData.setUploadTime(localDateTime);
        logger.debug("fileDataRepository is " + fileDataRepository);
        fileDataRepository.save(fileData);
        return fileData.getFileId();
    }

    public Response deleteFile(String fileId) {
        Response response = new Response();
        FileData fileData = fileDataRepository.findByFileId(fileId);
        if (fileData == null) {
            response.setAppInfo("file data not found");
            response.setAppStatus(AppConsts.RETURN_TRUE);
        } else {
            fileDataRepository.delete(fileData);
            response.setAppInfo("file data deleted :: " + fileId);
            response.setAppStatus(AppConsts.RETURN_TRUE);
        }
        return response;
    }

}