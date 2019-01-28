package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.Account;
import com.webox.common.model.User;

import org.bson.types.ObjectId;

public interface UserRepositoryCustom {

    public List<User> findByNestedAccountId(String accountId);

    public void updateNestedAccount(Account account);

    public User findUserByServiceId(ObjectId serviceId);

    public Account findAccountByUser(String UserId, String accountType);

}