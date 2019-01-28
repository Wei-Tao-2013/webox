package com.webox.common.repository;

import java.util.List;
import com.webox.common.model.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;

public class HotSpotRepositoryImpl implements HotSpotRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(HotSpotRepositoryCustom.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Service> loadHotSpotServicesByUser(String userId) {
        AggregationResults<Service> results = mongoTemplate
                .aggregate(newAggregation(match(where("userId").is(userId).and("status").is(true)),
                        sort(Direction.ASC, "rank"), lookup("service", "serviceId", "_id", "hotspot_services"),
                        unwind("hotspot_services"), replaceRoot("hotspot_services")), "hotSpot", Service.class);
        return results.getMappedResults();
    }

}