package com.webox.watchlistmodule.service;

import com.webox.common.model.Request;
import com.webox.common.model.Response;

public interface WatchListAPI{

    Response toggleWatchList(Request request);
    Response loadWatchListServices(Request request);
    Response loadWatchListServiceWithPostBriefList(Request request);
   
}