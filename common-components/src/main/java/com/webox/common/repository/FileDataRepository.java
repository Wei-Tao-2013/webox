package com.webox.common.repository;

import com.webox.common.model.FileData;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface FileDataRepository extends MongoRepository<FileData, String>{
    public FileData findByFileId(String fileId);

}