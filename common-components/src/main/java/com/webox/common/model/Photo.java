package com.webox.common.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Photo {
    @Id
    private String photoId;
    private String photoUrl;
    private String photoName;
    private LocalDateTime uploadTime;
    private LocalDateTime updateTime;
    private String status; // disabled, available
    @Indexed
    private String fileId; // FileData.fileId
}