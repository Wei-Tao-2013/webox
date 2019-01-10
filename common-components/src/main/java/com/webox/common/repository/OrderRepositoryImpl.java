package com.webox.common.repository;

import java.util.List;
import com.webox.common.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

  private static final Logger logger = LoggerFactory.getLogger(JobRepositoryImpl.class);

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public List<Order> loadOrdersByService(String serviceId) {
    AggregationResults<Order> results = mongoTemplate.aggregate(
        newAggregation(match(where("serviceId").is(serviceId)), sort(Direction.DESC, "orderBookedTime")), "order",
        Order.class);
    return results.getMappedResults();
  }

  @Override
  public List<Order> loadOrdersByVendor(String vendorId) {
    AggregationResults<Order> results = mongoTemplate.aggregate(
        newAggregation(match(where("vendorId").is(vendorId)), sort(Direction.DESC, "orderBookedTime")), "order",
        Order.class);
    return results.getMappedResults();
  }

  @Override
  public List<Order> loadOrdersByCustomer(String userId) {
    AggregationResults<Order> results = mongoTemplate.aggregate(
        newAggregation(match(where("userId").is(userId)), sort(Direction.DESC, "orderBookedTime")), "order",
        Order.class);
    return results.getMappedResults();
  }

  @Override
  public List<Order> loadOrdersByServiceAndUser(String serviceId, String userId) {
    AggregationResults<Order> results = mongoTemplate
        .aggregate(newAggregation(match(where("userId").is(userId).and("serviceId").is(serviceId)),
            sort(Direction.DESC, "orderBookedTime")), "order", Order.class);
    return results.getMappedResults();
  }

}