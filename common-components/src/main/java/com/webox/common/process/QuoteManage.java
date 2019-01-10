package com.webox.common.process;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.webox.common.model.AppMessage;
import com.webox.common.model.Job;
import com.webox.common.model.Quote;
import com.webox.common.model.Service;
import com.webox.common.repository.QuoteRepository;
import com.webox.common.repository.ServiceRepository;
import com.webox.common.utils.AppConsts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;

@Component("quoteManage")
public class QuoteManage {
  private static final Logger logger = LoggerFactory.getLogger(QuoteManage.class);

  @Autowired
  ServiceRepository serviceRepository;

  @Autowired
  QuoteRepository quoteRepository;

  @Autowired
  JobManage jobManage;

  @Autowired
  OrderManage orderManage;

  //private LocalDateTime localDateTime = LocalDateTime.now();
  private AppMessage appMessage = new AppMessage();

  // 0 requestSent, 1 quotePriceSent, 2 quoteAccept 3 quoteExpired 4,quoteRejected

  public String sendAQuote(Job job, Service service) {
    LocalDateTime localDateTime = LocalDateTime.now();
    Quote quote = new Quote();
    quote.setJobId(job.getJobId());
    quote.setQuoteContent(job.getJobDesc());
    quote.setServiceStartTime(job.getJobStartTime());
    quote.setServiceEndTime(job.getJobEndTime());
    quote.setServiceId(service.getServiceId());
    quote.setVendorId(service.getUserId());
    quote.setUserId(job.getUserId());
    quote.setServiceType(job.getServiceType());
    quote.setQuoteUpdateTime(localDateTime);
    quote.setQuoteRequestSentTime(localDateTime);
    quote.setStatus(0); // requestSent;
    quoteRepository.save(quote);
    return quote.getQuoteId();

  }

  public Quote loadAQuote(String quoteId) {
    return quoteRepository.findByQuoteId(quoteId);
  }

  public AppMessage quotePriceSent(Quote quote) { // action by vendor
    LocalDateTime localDateTime = LocalDateTime.now();
    if (this.quoteOperationCheck(quote, "PriceSent")) {
      quote.setStatus(1);
      quote.setQuotePriceSentTime(localDateTime);
      quote.setQuoteUpdateTime(localDateTime);
      quoteRepository.save(quote);
      this.appMessage.setMessage(AppConsts.QUOTE_PRICE_SENT + " [QUOTE ID]");
      this.appMessage.setMsgId("quote-01");
      this.appMessage.setMsgType("OK");
      this.appMessage.setDataReturn(quote.getQuoteId());
      return this.appMessage;
    } else {
      return this.appMessage;
    }
  }

  public AppMessage quoteAccept(Quote quote) { // action by customer
    LocalDateTime localDateTime = LocalDateTime.now();
    if (this.quoteOperationCheck(quote, "Accept")) {
      quote.setStatus(2);
      quote.setQuoteAcceptTime(localDateTime);
      quote.setQuoteUpdateTime(localDateTime);
      quoteRepository.save(quote);
      jobManage.assignAJob(quote.getJobId());
      // need generate order .......
      this.appMessage = orderManage.bookAnOrder(quote);
      return this.appMessage;
    } else {
      return this.appMessage;
    }

  }

  public AppMessage quoteExpired(Quote quote) { // form system or admin
    LocalDateTime localDateTime = LocalDateTime.now();
    if (this.quoteOperationCheck(quote, "Expired")) {
      quote.setStatus(3);
      quote.setQuoteExpiredTime(localDateTime);
      quote.setQuoteUpdateTime(localDateTime);
      quoteRepository.save(quote);
      this.appMessage.setMessage(AppConsts.QUOTE_EXPIRED + " [QUOTE ID]");
      this.appMessage.setMsgId("quote-03");
      this.appMessage.setMsgType("OK");
      this.appMessage.setDataReturn(quote.getQuoteId());
      return this.appMessage;
    } else {
      return this.appMessage;
    }
  }

  public AppMessage quoteRejected(Quote quote) { // by either user or vendor before it expiried
    LocalDateTime localDateTime = LocalDateTime.now();
    if (this.quoteOperationCheck(quote, "Reject")) {
      quote.setStatus(4);
      quote.setQuoteRjectedTime(localDateTime);
      quote.setQuoteUpdateTime(localDateTime);
      quoteRepository.save(quote);
      this.appMessage.setMessage(AppConsts.QUOTE_REJECTED + " [QUOTE ID] ");
      this.appMessage.setMsgId("quote-04");
      this.appMessage.setMsgType("OK");
      this.appMessage.setDataReturn(quote.getQuoteId());
      return this.appMessage;
    } else {
      return this.appMessage;
    }
  }

  public List<Quote> loadQuotesByService(String serviceId) { // for vendor view
    return quoteRepository.loadQuotesByService(serviceId);
  }

  public List<Quote> loadQuotesByServiceAndUser(String serviceId, String userId) { // for customer view
    return quoteRepository.loadQuotesByServiceAndUser(serviceId, userId);
  }

  public Map<String, List<Quote>> loadQuotesByUser(String userId) { // for customer view
    List<Quote> quoteList = quoteRepository.loadQuotesByCustomer(userId);
    Map<String, List<Quote>> map = quoteList.stream().collect(Collectors.groupingBy(Quote::getJobId));
    return map;
  }

  public Map<String, List<Quote>> loadQuotesByVendor(String vendorId) { // for vendor view
    List<Quote> quoteList = quoteRepository.loadQuotesByVendor(vendorId);
    Map<String, List<Quote>> map = quoteList.stream().collect(Collectors.groupingBy(Quote::getServiceId));
    return map;
  }

  private boolean quoteOperationCheck(Quote quote, String operation) {
    boolean permission = true;
    switch (operation) {
    case "PriceSent":
      if (quote.getStatus() != 0) {
        permission = false;
        this.appMessage.setMessage("This quote can't send price . [QUOTE STATUS] ");
        this.appMessage.setDataReturn(String.valueOf(quote.getStatus()));
        this.appMessage.setMsgType("ERR");
        this.appMessage.setMsgId("quote-err-01");
      } else {
        permission = this.checkWithJob(quote);
      }
      break;
    case "Accept":
      if (quote.getStatus() == 0) {
        permission = false;
        this.appMessage.setMessage("This quote has't got price [QUOTE STATUS] ");
        this.appMessage.setDataReturn(String.valueOf(quote.getStatus()));
        this.appMessage.setMsgType("ERR");
        this.appMessage.setMsgId("quote-err-02");
      } else {
        permission = this.checkWithJob(quote);
      }
      break;
    case "Reject":
      if (quote.getStatus() == 2) {
        permission = false;
        this.appMessage.setMessage("This quote has been accepted [QUOTE STATUS] ");
        this.appMessage.setDataReturn(String.valueOf(quote.getStatus()));
        this.appMessage.setMsgType("ERR");
        this.appMessage.setMsgId("quote-err-07");
        
      } else if (quote.getStatus() > 2){
        permission = false;
        this.appMessage.setMessage("This quote has been either rejected or expired already [QUOTE STATUS] ");
        this.appMessage.setDataReturn(String.valueOf(quote.getStatus()));
        this.appMessage.setMsgType("ERR");
        this.appMessage.setMsgId("quote-err-08");
      }

      break;
    case "Expired": // conditions of expired
      break;
    }
    return permission;
  }

  private boolean checkWithJob(Quote quote) {
    boolean permission = true;
    try {
      if (jobManage.loadAJob(quote.getJobId()).getStatus() == 2) {
        permission = false;
        this.appMessage.setMessage(
            "This quote has been taken by other vendor as job has been assigned . [JOB ID] ");
        this.appMessage.setDataReturn(quote.getJobId());
        this.appMessage.setMsgType("ERR");
        this.appMessage.setMsgId("quote-err-03");
      } else if (jobManage.loadAJob(quote.getJobId()).getStatus() == 3) {
        permission = false;
        this.appMessage.setMessage("The job has been canceled .[JOB ID] ");
        this.appMessage.setDataReturn(quote.getJobId());
        this.appMessage.setMsgType("ERR");
        this.appMessage.setMsgId("quote-err-05");

      }
    } catch (Exception e) {
      logger.error("eception occured when load job {}", quote.getJobId());
      permission = false;
      this.appMessage.setMessage("This quote's job cannot be found . [QUOTE ID] ");
      this.appMessage.setDataReturn(quote.getQuoteId());
      this.appMessage.setMsgType("ERR");
      this.appMessage.setMsgId("quote-err-04");
    }
    return permission;

  }

  private int getJobStatusByQuote(Quote quote) {
    return 0;
  }

  private List<Quote> loadQuotesByExample(Quote quote, ExampleMatcher matcher) {
    Example<Quote> example = Example.of(quote, matcher);
    List<Quote> quotesList = quoteRepository.findAll(example);
    quotesList.sort((a, b) -> {
      if (a.getQuoteUpdateTime().isAfter(b.getQuoteUpdateTime()))
        return 1;
      else
        return 0;
    });
    return quotesList;
  }

}