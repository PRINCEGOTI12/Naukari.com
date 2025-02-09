package com.project.jobportal.service;

import com.project.jobportal.Repository.JobSeekerApplyRepository;
import com.project.jobportal.Repository.JobSeekerSaveRepository;
import com.project.jobportal.entity.JobPostActivity;
import com.project.jobportal.entity.JobSeekerApply;
import com.project.jobportal.entity.JobSeekerProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerApplyService {
    private final JobSeekerApplyRepository jobSeekerApplyRepository;

    @Autowired
    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
    }

    public List<JobSeekerApply> getCandidatejob(JobSeekerProfile userId){
        return  jobSeekerApplyRepository.findByUserId(userId);
    }
    public List<JobSeekerApply> getjobCandidate(JobPostActivity job){
        return jobSeekerApplyRepository.findByJob(job);
    }
    public JobSeekerApply addNew(JobSeekerApply jobSeekerApply){
        return jobSeekerApplyRepository.save(jobSeekerApply);
    }


}
