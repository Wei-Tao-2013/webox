package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.PostInfo;
import com.webox.common.model.Service;
import com.webox.common.model.WatchList;
public interface WatchListRepositoryCustom{
    public List<Service> loadWatchListServicesByUser(String userId);
    public List<PostInfo> loadWatchListPostInfosByUser(String userId);
    
   
}