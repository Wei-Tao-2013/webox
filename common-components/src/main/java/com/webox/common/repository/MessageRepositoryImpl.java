package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.Message;
import com.webox.common.model.MessageCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;

public class MessageRepositoryImpl implements MessageRepositoryCustom{
   
  private static final Logger logger = LoggerFactory.getLogger(MessageRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<MessageCount> unReadMessageCount(String receiver) {
      AggregationResults<MessageCount> results = mongoTemplate.aggregate(
        newAggregation(
            match(where("receiver").is(receiver).and("msgStatus").is(0).and("msgChannel").is("service")),
            group("userId").count().as("total"),
            project("total").and("userId").previousOperation(),
			      sort(Direction.DESC, "serviceId")
        ),Message.class, MessageCount.class);
      return results.getMappedResults();
    }

    /*
    public List<MessageCount> unReadMessageCount(String receiver) {
      AggregationResults<MessageCount> results = mongoTemplate.aggregate(
        newAggregation(
            match(where("receiver").is(receiver).and("msgStatus").is(0)),
            group("serviceId").count().as("total"),
            project("total").and("serviceId").previousOperation(),
			      sort(Direction.DESC, "serviceId")
        ),Message.class, MessageCount.class);
      return results.getMappedResults();
    }
    */
  
  } 