package com.webox.common.repository;

import java.util.List;
import com.webox.common.model.PostInfo;
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

public class PostInfoRepositoryImpl implements PostInfoRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(PostInfoRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void deleteNestedPhoto(String fileId) {
        Query query = Query.query(Criteria.where("postPhoto").elemMatch(Criteria.where("fileId").is(fileId)));
        Update update = new Update().pull("postPhoto", new BasicDBObject("fileId", fileId));
        mongoTemplate.updateMulti(query, update, PostInfo.class);
    }

    @Override
    public List<PostInfo> loadPostInfosBriefsByService(String serviceId, int limit) {
        AggregationResults<PostInfo> results = mongoTemplate
                .aggregate(newAggregation(match(where("serviceId").is(serviceId).and("status").is("PUBLISHED")),
                        sort(Direction.DESC, "postUpdateDateTime"), limit(limit)), "postInfo", PostInfo.class);
        return results.getMappedResults();
    }

    @Override
    public List<PostInfo> loadPostInfosByService(String serviceId, String status) {
        if ("ALL".equals(status)) {
            AggregationResults<PostInfo> results = mongoTemplate.aggregate(
                    newAggregation(match(where("serviceId").is(serviceId)), sort(Direction.DESC, "postUpdateDateTime")),
                    "postInfo", PostInfo.class);
            return results.getMappedResults();
        } else {
            AggregationResults<PostInfo> results = mongoTemplate
                    .aggregate(newAggregation(match(where("serviceId").is(serviceId).and("status").is(status)),
                            sort(Direction.DESC, "postUpdateDateTime")), "postInfo", PostInfo.class);
            return results.getMappedResults();
        }

    }

}