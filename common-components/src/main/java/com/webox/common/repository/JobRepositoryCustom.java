package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.Job;

public interface JobRepositoryCustom {
    public List<Job> loadJobsByUser(String userId, int status);

}