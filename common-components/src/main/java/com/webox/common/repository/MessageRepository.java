package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.Message;

import org.springframework.data.mongodb.repository.MongoRepository;
public interface MessageRepository extends MongoRepository<Message, String>, MessageRepositoryCustom { 
    
   
}