package com.webox.common.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import com.webox.common.model.Quote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

public class QuoteRepositoryImpl implements QuoteRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(HotSpotRepositoryCustom.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Quote> loadQuotesByService(String serviceId) {
        AggregationResults<Quote> results = mongoTemplate
                .aggregate(newAggregation(match(where("serviceId").is(serviceId).and("status").lt(3)),
                        sort(Direction.DESC, "quoteUpdateTime")), "quote", Quote.class);
        return results.getMappedResults();
    }

    @Override
    public List<Quote> loadQuotesByVendor(String vendorId) {
        AggregationResults<Quote> results = mongoTemplate
                .aggregate(newAggregation(match(where("vendorId").is(vendorId).and("status").lt(3)),
                        sort(Direction.DESC, "quoteUpdateTime")), "quote", Quote.class);
        return results.getMappedResults();
    }

    @Override
    public List<Quote> loadQuotesByCustomer(String userId) {
        AggregationResults<Quote> results = mongoTemplate
                .aggregate(newAggregation(match(where("userId").is(userId).and("status").lt(3)),
                        sort(Direction.DESC, "quoteUpdateTime")), "quote", Quote.class);
        return results.getMappedResults();
    }

    @Override
    public List<Quote> loadQuotesByServiceAndUser(String serviceId, String userId) {
        AggregationResults<Quote> results = mongoTemplate.aggregate(
                newAggregation(match(where("serviceId").is(serviceId).and("status").lt(3).and("userId").is(userId)),
                        sort(Direction.DESC, "quoteUpdateTime")),
                "quote", Quote.class);
        return results.getMappedResults();
    }

}