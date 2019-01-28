package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.PostInfo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostInfoRepository extends MongoRepository<PostInfo, String>, PostInfoRepositoryCustom {

    public List<PostInfo> findByServiceId(String serviceId);

    public PostInfo findByPostInfoId(String postInfoId);

}