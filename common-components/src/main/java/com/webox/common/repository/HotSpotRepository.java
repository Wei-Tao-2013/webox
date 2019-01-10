package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.HotSpot;

import org.springframework.data.mongodb.repository.MongoRepository;
public interface HotSpotRepository extends MongoRepository<HotSpot, String>, HotSpotRepositoryCustom{
    public HotSpot findByHotSpotId(String hotSpotId);
    public List<HotSpot> findByUserId(String UserId);
   
}