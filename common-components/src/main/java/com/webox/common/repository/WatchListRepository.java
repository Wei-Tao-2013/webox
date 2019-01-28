package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.WatchList;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WatchListRepository extends MongoRepository<WatchList, String>, WatchListRepositoryCustom {
    public WatchList findByWatchListId(String watchListId);

    public List<WatchList> findByUserId(String userId);

    public List<WatchList> findByServiceId(ObjectId serviceId);

}