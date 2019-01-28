package com.webox.common.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.replaceRoot;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import com.webox.common.model.PostInfo;
import com.webox.common.model.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

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