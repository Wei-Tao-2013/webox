package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.ServiceType;

import org.springframework.data.mongodb.repository.MongoRepository;
public interface ServiceTypeRepository extends MongoRepository<ServiceType, String> { 
    
   // public List<ServiceType> findByTypeId(String typeId);
   public List<ServiceType> findByTypeName(String typeName);
}