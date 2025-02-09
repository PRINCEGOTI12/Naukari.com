package com.project.jobportal.entity;


public class RecruiterjobsDto {

    private Long TotalCandidates;
    private Integer jobPostId;
    private String jobTitle;
    private JobLocation jobLocationId;
    private JobCompany jobCompanyId;

    public RecruiterjobsDto(Long totalCandidates, Integer jobPostId, String jobTitle, JobLocation jobLocationId, JobCompany jobCompany) {
        TotalCandidates = totalCandidates;
        this.jobPostId = jobPostId;
        this.jobTitle = jobTitle;
        this.jobLocationId = jobLocationId;
        this.jobCompanyId = jobCompany;
    }

    public Long getTotalCandidates() {
        return TotalCandidates;
    }

    public void setTotalCandidates(Long totalCandidates) {
        TotalCandidates = totalCandidates;
    }

    public Integer getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(Integer jobPostId) {
        this.jobPostId = jobPostId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public JobLocation getJobLocationId() {
        return jobLocationId;
    }

    public void setJobLoicationId(JobLocation jobLocation) {
        this.jobLocationId = jobLocation;
    }

    public JobCompany getJobCompanyId() {
        return jobCompanyId;
    }

    public void setJobCompanyId(JobCompany jobCompany) {
        this.jobCompanyId = jobCompany;
    }
}
