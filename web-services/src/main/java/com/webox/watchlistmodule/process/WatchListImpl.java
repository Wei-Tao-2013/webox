package com.webox.watchlistmodule.process;

import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.process.HotSpotManage;
import com.webox.common.process.ServiceManage;
import com.webox.common.process.WatchListManage;
import com.webox.watchlistmodule.service.WatchListAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class WatchListImpl implements WatchListAPI {

    private static final Logger logger = LoggerFactory.getLogger(WatchListImpl.class);

    @Autowired @Qualifier("watchListManage")
	private WatchListManage watchListManage;
	
	@Autowired @Qualifier("generalServiceManage")
    private ServiceManage serviceManage;
   
    @Override
	public Response toggleWatchList(Request request) {
	    return watchListManage.toggleWatchList(request.getUserId(), request.getServiceIdList());
	}

	@Override
	public Response loadWatchListServices(Request request) {
		return watchListManage.loadWatchListService(request.getUserId());
		//return serviceManage.loadHotSpotServicesForUser(request.getUserId());
	}

	@Override
	public Response loadWatchListServiceWithPostBriefList(Request request) {
		return watchListManage.loadWatchListServiceWithPostBriefList(request.getUserId(), request.getLimit());
	}

}