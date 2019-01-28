package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.Quote;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuoteRepository extends MongoRepository<Quote, String>, QuoteRepositoryCustom {

    public List<Quote> findByUserId(String UserId);

    public Quote findByQuoteId(String JobId);

}