package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;

public class JobRepositoryImpl implements JobRepositoryCustom {

  private static final Logger logger = LoggerFactory.getLogger(JobRepositoryImpl.class);

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public List<Job> loadJobsByUser(String userId, int status) {
    if (status < 99) {
      AggregationResults<Job> results = mongoTemplate
          .aggregate(newAggregation(match(where("userId").is(userId).and("status").is(status)),
              sort(Direction.DESC, "jobCreatedTime")), "job", Job.class);
      return results.getMappedResults();
    } else { // all
      AggregationResults<Job> results = mongoTemplate
          .aggregate(newAggregation(match(where("userId").is(userId).and("status").ne(3)), // exculding cancel job
              sort(Direction.DESC, "jobCreatedTime")), "job", Job.class);
      return results.getMappedResults();
    }

  }

}