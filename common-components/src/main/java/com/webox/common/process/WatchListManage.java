package com.webox.common.process;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.webox.common.model.PostInfo;
import com.webox.common.model.WatchList;
import com.webox.common.model.Response;
import com.webox.common.repository.WatchListRepository;
import com.webox.common.repository.ServiceRepository;
import com.webox.common.utils.AppConsts;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

@Component("watchListManage")
public class WatchListManage {
    private static final Logger logger = LoggerFactory.getLogger(WatchListManage.class);

    @Autowired
    WatchListRepository watchListRepository;

    @Autowired
    @Qualifier("generalServiceManage")
    ServiceManage serviceManage;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    PostInfoManage postInfoManage;

//    public WatchList getWatchList(String watchListId) {
  //      WatchList watchList = watchListRepository.findByWatchListId(watchListId);
    //    return watchList;
   // }

    public WatchList toggleWatchList(String userId, String serviceId) {
        WatchList WatchList = new WatchList(userId, serviceId);
        Example<WatchList> example = Example.of(WatchList);
        LocalDateTime localDateTime = LocalDateTime.now();
        Optional<WatchList> WatchListOptional = watchListRepository.findOne(example);
        if (WatchListOptional.isPresent()) {
            WatchList = WatchListOptional.get();
            WatchList.setStatus(!WatchList.getStatus());
        } else {
            WatchList.setStatus(true);
        }
        WatchList.setUpdateTime(localDateTime);
        watchListRepository.save(WatchList);
        return WatchList;
    }

    public Response toggleWatchList(String userId, List<String> serviceIdList) {
        Response response = new Response();
        List<WatchList> WatchList = new ArrayList<WatchList>();
        serviceIdList.forEach(serviceId -> {
            WatchList.add(this.toggleWatchList(userId, serviceId));
        });
        response.setAppStatus(AppConsts.RETURN_TRUE);
        response.setAppInfo("toggle WatchList");
        response.setWatchList(WatchList);
        return response;
    }

    public List<String> loadUserIdListByWatchService(String serviceId){
        List<String> userIds = new ArrayList<String>();
        List<WatchList> WatchLists = watchListRepository.findByServiceId(new ObjectId(serviceId));
        WatchLists.forEach(o->{
            userIds.add(o.getUserId());
        });
        return userIds;
    }

    public Response loadWatchListService(String userId) {
        Response response = new Response();
        response.setServices(watchListRepository.loadWatchListServicesByUser(userId));
        response.setUsers(serviceManage.getVendorListbyServiceList(response.getServices()));
        response.setAppInfo("WatchList service loaded");
        response.setAppStatus(AppConsts.RETURN_TRUE);
        return response;
    }

    public Response loadWatchListServiceWithPostBriefList(String userId, int limit) {
        Response response = new Response();
        response.setServices(watchListRepository.loadWatchListServicesByUser(userId));
        Map<String, List<PostInfo>> postInfoMap = postInfoManage.loadLimitPostInfosByServices(response.getServices(),
                limit);
        List<PostInfo> postInfoList = new ArrayList<PostInfo>();
        postInfoMap.forEach((k, v) -> {
            v.forEach(p -> {
                p.setPostContent(null);
                p.setPostPhoto(null);
                postInfoList.add(p);
            });
        });
        response.setPostInfos(postInfoList);
        response.setUsers(serviceManage.getVendorListbyServiceList(response.getServices()));
        response.setAppInfo("WatchList with Posts Loaded");
        response.setAppStatus(AppConsts.RETURN_TRUE);
        return response;
    }

}