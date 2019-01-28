package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

    public List<User> findByPrimaryEmail(String primaryEmail);

    public User findByUserId(String userId);

}