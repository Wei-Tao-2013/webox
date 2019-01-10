package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.PostInfo;

public interface PostInfoRepositoryCustom{
    public void deleteNestedPhoto(String fileId);
    public List<PostInfo> loadPostInfosBriefsByService(String serviceId, int limit);
    public List<PostInfo> loadPostInfosByService(String serviceId,String status);
}