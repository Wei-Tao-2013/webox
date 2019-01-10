package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.PostInfo;
import com.webox.common.model.Service;
import com.webox.common.model.WatchList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;

public class WatchListRepositoryImpl implements WatchListRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(WatchListRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Service> loadWatchListServicesByUser(String userId) {
        AggregationResults<Service> results = mongoTemplate
                .aggregate(newAggregation(match(where("userId").is(userId).and("status").is(true)),
                        sort(Direction.ASC, "rank"), lookup("service", "serviceId", "_id", "watchlist_services"),
                        unwind("watchlist_services"), replaceRoot("watchlist_services")), "watchList", Service.class);
        return results.getMappedResults();
    }

    @Override
    public List<PostInfo> loadWatchListPostInfosByUser(String userId) {
        AggregationResults<PostInfo> results = mongoTemplate
                .aggregate(newAggregation(match(where("userId").is(userId).and("status").is(true)),
                        sort(Direction.ASC, "rank"), lookup("postInfo", "serviceId", "serviceId", "watchlist_postInfo"),
                        unwind("watchlist_postInfo"), replaceRoot("watchlist_postInfo")), "watchList", PostInfo.class);
        return results.getMappedResults();
    }

   

}