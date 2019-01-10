package com.webox.common.process;

import java.time.LocalDateTime;
import java.util.List;

import com.webox.common.model.AppMessage;
import com.webox.common.model.Job;
import com.webox.common.model.Request;

import com.webox.common.repository.JobRepository;

import com.webox.common.utils.GIutils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("jobManage")
public class JobManage {
  private static final Logger logger = LoggerFactory.getLogger(ServiceManage.class);

  @Autowired
  JobRepository jobRepository;

  // private LocalDateTime localDateTime = LocalDateTime.now();
  private AppMessage appMessage = new AppMessage();

  // 0 JobCreated, 1,jobUpdated, 2 JobDone ,3 JobCancel

  public AppMessage postAJob(Request request) {
    LocalDateTime localDateTime = LocalDateTime.now();
    Job job = new Job();
    job = request.getJob();
    String jobId = job.getJobId();
    job.setJobCreatedTime(localDateTime);
    job.setJobStartTime(job.getJobStartTime());
    job.setJobEndTime(job.getJobEndTime());
    job.setStatus(0);
    Job existingJob = jobRepository.findByJobId(jobId);

    if (existingJob != null) {
      if (this.jobOperationCheck(job, "updateAjob")) {
        GIutils.copyProperties(job, existingJob);
        job.setUpdatedTime(localDateTime);
        job.setJobUpdateTime(localDateTime);
        jobRepository.save(existingJob);
        this.appMessage.setMessage("JOB_UPDATED" + " [JOB ID]");
        this.appMessage.setMsgId("job-01");
        this.appMessage.setMsgType("OK");
        this.appMessage.setDataReturn(job.getJobId());

      } else { // job can't be updated
        // nothing
      }

    } else {
      job.setJobCreatedTime(localDateTime);
      job.setUpdatedTime(localDateTime);
      job.setStatus(0); // job created
      jobRepository.save(job);
      this.appMessage.setMessage("JOB_CREATED" + "[JOB ID]");
      this.appMessage.setMsgId("job-02");
      this.appMessage.setMsgType("OK");
      this.appMessage.setDataReturn(job.getJobId());
    }

    return this.appMessage;
  }

  public AppMessage assignAJob(String jobId) { // job done means order is dispatched , it should be called by
                                               // application
    Job existingJob = jobRepository.findByJobId(jobId);
    LocalDateTime localDateTime = LocalDateTime.now();
    if (existingJob != null) {
      if (this.jobOperationCheck(existingJob, "assignAJob")) {
        existingJob.setStatus(2);
        existingJob.setUpdatedTime(localDateTime);
        jobRepository.save(existingJob);
        this.appMessage.setMsgId("job-03");
        this.appMessage.setMsgType("OK");
        this.appMessage.setMessage("JOB_ASSIGNED [JOB ID]");
        this.appMessage.setDataReturn(existingJob.getJobId());
      }
    } else {
      this.appMessage.setMsgId("job-err-02");
      this.appMessage.setMsgType("ERR");
      this.appMessage.setMessage("Job can't be found [JOB ID] ");
      this.appMessage.setDataReturn(jobId);
    }
    return this.appMessage;
  }

  public AppMessage cancelAJob(String jobId) { // cancel a job means drop all quotes sent out before job done
    Job existingJob = jobRepository.findByJobId(jobId);
    LocalDateTime localDateTime = LocalDateTime.now();
    if (existingJob != null) {
      if (this.jobOperationCheck(existingJob, "cancelAJob")) {
        existingJob.setStatus(3);
        existingJob.setUpdatedTime(localDateTime);
        jobRepository.save(existingJob);
        this.appMessage.setMsgId("job-04");
        this.appMessage.setMsgType("OK");
        this.appMessage.setMessage("JOB_CANCEL [JOB ID]");
        this.appMessage.setDataReturn(existingJob.getJobId());

      }
    } else {
      this.appMessage.setMsgId("job-err-03");
      this.appMessage.setMsgType("ERR");
      this.appMessage.setMessage("Job can't be found [JOB ID] ");
      this.appMessage.setDataReturn(jobId);
    }
    return this.appMessage;
  }

  public Job loadAJob(String jobId) {
    return jobRepository.findByJobId(jobId);
  }

  public List<Job> loadJobListByUser(String userId, int status) {
    return jobRepository.loadJobsByUser(userId, status);
  };

  private boolean jobOperationCheck(Job job, String operation) {
    boolean permission = true;
    switch (operation) {
    case "cancelAJob":
      if (job.getStatus() > 1) { // still need check other situation like quote and order
        this.appMessage.setMsgId("job_err_04");
        this.appMessage.setMessage("Job has been either Done or Cancel already. [JOB STATUS] ");
        this.appMessage.setMsgType("ERR");
        this.appMessage.setDataReturn(String.valueOf(job.getStatus()));
        permission = false;
      }
      break;
    case "assignAJob":
      if (job.getStatus() > 1) { // still need check other situation like quote and order
        this.appMessage.setMsgId("job_err_04");
        this.appMessage.setMessage("Job has been either Done or Cancel already. [JOB STATUS] ");
        this.appMessage.setDataReturn(String.valueOf(job.getStatus()));
        this.appMessage.setMsgType("ERR");
        permission = false;
      }
      break;
    case "createAjob":
      break;
    case "updateAjob":
      break;
    }
    return permission;
  }

}