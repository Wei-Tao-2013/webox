package com.webox.common.model;


import java.time.LocalDateTime;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data  @NoArgsConstructor
public class FileData{

   private @Id String  fileId;
   private Binary fileData;
   private LocalDateTime  uploadTime;
  
}