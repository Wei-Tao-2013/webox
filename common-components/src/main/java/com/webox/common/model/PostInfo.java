package com.webox.common.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="postInfo")
@Data @NoArgsConstructor
public class PostInfo {
    
    private @Id String postInfoId;
    @Indexed
    private String serviceId;
    private String postTitle;
    private List<Photo> postPhoto = new ArrayList<Photo>();
    private LocalDateTime postDateTime;
    private LocalDateTime postUpdateDateTime;
    private int readCounts = 0;   
    private int agreeCounts = 0;
    private String postContent;
    @Indexed
    private String status;  // Drafted, Published, Updated, Disabled

    public PostInfo addPostPhoto(Photo photo) {
        this.postPhoto.add(photo);
        return this;
    }

    public PostInfo deletePostPhoto(Photo photo){
       return this;
    }

   

}