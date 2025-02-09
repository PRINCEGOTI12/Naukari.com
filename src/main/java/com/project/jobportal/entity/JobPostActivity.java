package com.project.jobportal.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name="job_post_activity")
public class JobPostActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobPostId;

    
    private String descriptionOfJob;
    private String jobTitle;
    private String jobType;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date postedDate;

    private String remote;
    private String salary;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "jobCompanyId",referencedColumnName = "Id")
    private JobCompany  jobCompanyId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "jobLocationId",referencedColumnName = "Id")
    private JobLocation jobLocationId;

    @ManyToOne
    @JoinColumn(name = "postedById",referencedColumnName = "userId")
    private Users postedById;

    @Transient
    private Boolean isActive;

    @Transient
    private Boolean isSaved;


    public JobPostActivity() {
    }

    public JobPostActivity(Integer jobPostId, String descriptionOfJob, String jobTitle,
                           String jobType, Date postedDate, String remote, String salary,
                           JobCompany jobCompanyId, JobLocation jobLocationId, Users postedById,
                           Boolean isActive, Boolean isSaved) {
        this.jobPostId = jobPostId;
        this.descriptionOfJob = descriptionOfJob;
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.postedDate = postedDate;
        this.remote = remote;
        this.salary = salary;
        this.jobCompanyId = jobCompanyId;
        this.jobLocationId = jobLocationId;
        this.postedById = postedById;
        this.isActive = isActive;
        this.isSaved = isSaved;
    }

    public Integer getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(Integer jobPostId) {
        this.jobPostId = jobPostId;
    }

    public String getDescriptionOfJob() {
        return descriptionOfJob;
    }

    public void setDescriptionOfJob(String descriptionOfJob) {
        this.descriptionOfJob = descriptionOfJob;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public JobCompany getJobCompanyId() {
        return jobCompanyId;
    }

    public void setJobCompanyId(JobCompany jobCompanyId) {
        this.jobCompanyId = jobCompanyId;
    }

    public JobLocation getJobLocationId() {
        return jobLocationId;
    }

    public void setJobLocationId(JobLocation jobLocationId) {
        this.jobLocationId = jobLocationId;
    }

    public Users getPostedById() {
        return postedById;
    }

    public void setPostedById(Users postedById) {
        this.postedById = postedById;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Boolean getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(Boolean saved) {
        isSaved = saved;
    }

    @Override
    public String toString() {
        return "JobPostActivity{" +
                "jobPostId=" + jobPostId +
                ", descriptionOfJob='" + descriptionOfJob + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", jobType='" + jobType + '\'' +
                ", postedDate=" + postedDate +
                ", remote='" + remote + '\'' +
                ", salary='" + salary + '\'' +
                ", jobCompanyId=" + jobCompanyId +
                ", jobLocationId=" + jobLocationId +
                ", postedById=" + postedById +
                ", isActive=" + isActive +
                ", isSaved=" + isSaved +
                '}';
    }
}
