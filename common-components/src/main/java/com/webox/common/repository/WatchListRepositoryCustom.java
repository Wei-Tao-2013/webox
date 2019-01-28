package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.PostInfo;
import com.webox.common.model.Service;

public interface WatchListRepositoryCustom {
    public List<Service> loadWatchListServicesByUser(String userId);

    public List<PostInfo> loadWatchListPostInfosByUser(String userId);

}