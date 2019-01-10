package com.webox.common.repository;

import java.util.List;
import com.webox.common.model.Account;
import com.webox.common.model.User;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class UserRepositoryImpl implements UserRepositoryCustom {

  private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

  @Autowired
  private MongoTemplate mongoTemplate;

  public List<User> findByNestedAccountId(String accountId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("userAccount.accountId").is(accountId));
    return (List<User>) mongoTemplate.find(query, User.class);
  }

  public void updateNestedAccount(Account account) {
    Query query = new Query(Criteria.where("userAccount.accountId").is(account.getAccountId()));
    Update update = new Update().set("userAccount.$.visitNumber", account.getVisitNumber())
        .set("userAccount.$.lastTimeLogin", account.getLastTimeLogin())
        .set("userAccount.$.lastTimeLogout", account.getLastTimeLogout())
        .set("userAccount.$.accStatus", account.getAccStatus())
        .set("userAccount.$.accName", account.getAccName())
        .set("userAccount.$.accAvatar",account.getAccAvatar());
    mongoTemplate.updateFirst(query, update, User.class);
  }



  @Override
  public User findUserByServiceId(ObjectId serviceId) {
    return null;
  }

  @Override
  public Account findAccountByUser(String UserId, String accountType) {
    return null;
  }

}