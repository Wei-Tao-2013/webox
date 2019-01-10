package com.webox.common.repository;

import java.util.List;
import com.webox.common.model.Service;
import com.mongodb.BasicDBObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;

public class ServiceRepositoryImpl implements ServiceRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void deleteNestedPhoto(String fileId) {
        Query query = Query.query(Criteria.where("servicePhoto").elemMatch(Criteria.where("fileId").is(fileId)));
        Update update = new Update().pull("servicePhoto", new BasicDBObject("fileId", fileId));
        mongoTemplate.updateMulti(query, update, Service.class);
    }

    @Override
    public List<Service> loadHotSpotServicesForUser(String userId) {
        AggregationResults<Service> results = mongoTemplate
                .aggregate(newAggregation(match(where("userId").ne(userId).and("status").is("APPROVED")),
                        sort(Direction.ASC, "rank")), "service", Service.class);
        return results.getMappedResults();
    }

    @Override
    public List<Service> searchServiceByTag() {
        AggregationResults<Service> results = mongoTemplate.aggregate(
                newAggregation(match(where("status").is("APPROVED")), sort(Direction.ASC, "rank")), "service",
                Service.class);
        return results.getMappedResults();
    }

}