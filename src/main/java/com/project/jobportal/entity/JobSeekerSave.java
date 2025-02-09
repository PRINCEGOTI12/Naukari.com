package com.project.jobportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job_seeker_save",uniqueConstraints = {@UniqueConstraint(columnNames = {"userId,job"})})
public class JobSeekerSave {

    @Id
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job",referencedColumnName = "jobPostId")
    private JobPostActivity job;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId",referencedColumnName = "user_account_id")
    private JobSeekerProfile userId;

    public JobSeekerSave() {
    }

    public JobSeekerSave(Integer id, JobPostActivity job, JobSeekerProfile userId) {
        this.id = id;
        this.job = job;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return "JobSeekerSave{" +
                "id=" + id +
                ", job=" + job +
                ", userId=" + userId +
                '}';
    }
}