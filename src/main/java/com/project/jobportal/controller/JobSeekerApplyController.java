package com.project.jobportal.controller;

import com.project.jobportal.Repository.Usersrepository;
import com.project.jobportal.entity.*;
import com.project.jobportal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class JobSeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    private final UsersService usersService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerProfileService jobSeekerProfileService;

    @Autowired
    public JobSeekerApplyController(JobPostActivityService jobPostActivityService, UsersService usersService,
                                        JobSeekerApplyService jobSeekerApplyService,JobSeekerSaveService jobSeekerSaveService,
                                    RecruiterProfileService recruiterProfileService, JobSeekerProfileService jobSeekerProfileService) {
        this.jobPostActivityService = jobPostActivityService;
        this.usersService = usersService;
        this.jobSeekerApplyService=jobSeekerApplyService;
        this.jobSeekerSaveService=jobSeekerSaveService;
        this.recruiterProfileService=recruiterProfileService;
        this.jobSeekerProfileService=jobSeekerProfileService;
    }

    @GetMapping("job-details-apply/{id}")
    public String applyjob(@PathVariable("id")int id, Model model){
        JobPostActivity jobPostActivity=jobPostActivityService.getone(id);

        List<JobSeekerApply> applyList=jobSeekerApplyService.getjobCandidate(jobPostActivity);
        List<JobSeekerSave> saveList=jobSeekerSaveService.getJobCandidate(jobPostActivity);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                RecruiterProfile user=recruiterProfileService.getCurrentRecruiterProfile();
                if(user!=null){
                    model.addAttribute("applyList",applyList);
                }
            }
            else {
                JobSeekerProfile user=jobSeekerProfileService.getCurrentJobSeekerProfile();
                if(user!=null){
                    boolean exist=false;
                    boolean saved=false;
                    for(JobSeekerApply jobSeekerApply:applyList){
                        if(jobSeekerApply.getUserId().getUserAccountId()==user.getUserAccountId()){
                            exist=true;
                            break;
                        }
                    }
                    for(JobSeekerSave jobSeekerSave:saveList){
                        if(jobSeekerSave.getUserId().getUserAccountId()== user.getUserAccountId()){
                            saved=true;
                            break;
                        }
                    }
                    model.addAttribute("alreadyApplied",exist);
                    model.addAttribute("alreadySaved",saved);
                }
            }
        }
        JobSeekerApply jobSeekerApply=new JobSeekerApply();
        model.addAttribute("applyJob",jobSeekerApply);
        model.addAttribute("jobDetails",jobPostActivity);
        model.addAttribute("user",usersService.getCurrentUserProfile());
        return "job-details";
    }

    @PostMapping("job-details/apply/{id}")
    public String apply(@PathVariable("id") int id,JobSeekerApply jobSeekerApply){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentusrname=authentication.getName();
            Users users=usersService.getuserbyemail(currentusrname).orElseThrow(()->new UsernameNotFoundException("user not found"));
            Optional<JobSeekerProfile> jobSeekerProfile=jobSeekerProfileService.getUserId(users.getUserId());
            JobPostActivity jobPostActivity=jobPostActivityService.getone(id);
            if(jobSeekerProfile.isPresent() && jobPostActivity !=null){
                jobSeekerApply=new JobSeekerApply();
                jobSeekerApply.setApplyDate(new Date());
                jobSeekerApply.setUserId(jobSeekerProfile.get());
                jobSeekerApply.setJob(jobPostActivity);
            }else {
                throw new RuntimeException("User not found");
            }
            jobSeekerApplyService.addNew(jobSeekerApply);
        }
        return "redirect:/dashboard/";
    }
}
