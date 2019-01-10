package com.webox.common.repository;
import java.util.List;

import com.webox.common.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface JobRepository extends MongoRepository<Job, String> , JobRepositoryCustom{

   // public List<Job> findByUserId(String UserId);
    public Job findByJobId(String JobId);

}