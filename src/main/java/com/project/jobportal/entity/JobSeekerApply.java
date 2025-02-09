package com.project.jobportal.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="job_seeker_apply",uniqueConstraints = {@UniqueConstraint(columnNames ={ "job,userId"})})
public class JobSeekerApply implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date applyDate;

    private String coverLetter;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job",referencedColumnName = "jobPostId")
    private JobPostActivity job;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId",referencedColumnName = "user_account_id")
    private JobSeekerProfile userId;


    public JobSeekerApply() {
    }

    public JobSeekerApply(Integer id, Date applyDate, String coverLetter, JobPostActivity job, JobSeekerProfile userId) {
        this.id = id;
        this.applyDate = applyDate;
        this.coverLetter = coverLetter;
        this.job = job;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public JobPostActivity getJob() {
        return job;
    }

    public void setJob(JobPostActivity job) {
        this.job = job;
    }

    public JobSeekerProfile getUserId() {
        return userId;
    }

    public void setUserId(JobSeekerProfile userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "JobSeekerApply{" +
                "id=" + id +
                ", applyDate=" + applyDate +
                ", coverLetter='" + coverLetter + '\'' +
                ", job=" + job +
                ", userId=" + userId +
                '}';
    }
}
