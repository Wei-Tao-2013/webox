package com.webox.common.repository;
import java.util.List;

import com.webox.common.model.Service;

import org.springframework.data.mongodb.repository.MongoRepository;
public interface ServiceRepository extends MongoRepository<Service, String>, ServiceRepositoryCustom {
    
    public List<Service> findByUserId(String UserId);
    public Service findByServiceId(String ServiceId);
      
}