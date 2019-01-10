package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.Quote;

public interface QuoteRepositoryCustom {
    public List<Quote> loadQuotesByService(String serviceId);

    public List<Quote> loadQuotesByVendor(String userId);

    public List<Quote> loadQuotesByCustomer(String userId);

    public List<Quote> loadQuotesByServiceAndUser(String serviceId, String userId);

}