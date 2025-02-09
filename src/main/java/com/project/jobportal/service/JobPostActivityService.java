package com.project.jobportal.service;

import com.project.jobportal.Repository.JobPostActivityRepository;
import com.project.jobportal.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class JobPostActivityService {
    private final JobPostActivityRepository jobPostActivityRepository;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final JobSeekerApplyService jobSeekerApplyService;

    @Autowired
    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository,
                                  JobSeekerApplyService jobSeekerApplyService,JobSeekerSaveService jobSeekerSaveService) {
        this.jobPostActivityRepository = jobPostActivityRepository;
        this.jobSeekerApplyService=jobSeekerApplyService;
        this.jobSeekerSaveService=jobSeekerSaveService;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity) {
        return jobPostActivityRepository.save(jobPostActivity);
    }

    public List<RecruiterjobsDto> getRecruiterjob(int recruiter){
        List<IRecruiterJobs>  recruiterJobs=jobPostActivityRepository.getRecruiterjob(recruiter);

        List<RecruiterjobsDto> recruiterjobsDtos=new ArrayList<>();

        for(IRecruiterJobs rec: recruiterJobs){
            JobLocation loc=new JobLocation(rec.getLocationId(),rec.getCity(),rec.getState(),rec.getCountry());
            JobCompany com=new JobCompany(rec.getCompanyId(),rec.getName(),"");
            recruiterjobsDtos.add(new RecruiterjobsDto(rec.getTotalCandidates(),rec.getJob_post_id(),rec.getJob_title(),loc,com));
        }
        return  recruiterjobsDtos;

    }
    public JobPostActivity getone(int id){
        return jobPostActivityRepository.findById(id).orElseThrow(()->new RuntimeException("job not found"));
    }

    public List<JobPostActivity> getAll() {
        return jobPostActivityRepository.findAll();
    }

    public List<JobPostActivity> search(String job, String location, List<String> type, List<String> remotetype, LocalDate searchdate) {
        return Objects.isNull(searchdate) ? jobPostActivityRepository.searchwithoutdate(job,location,type,remotetype) : jobPostActivityRepository.searchwithdate(job,location,type,remotetype,searchdate);
    }

}
