package com.project.jobportal.controller;

import com.project.jobportal.entity.*;
import com.project.jobportal.service.JobPostActivityService;
import com.project.jobportal.service.JobSeekerProfileService;
import com.project.jobportal.service.JobSeekerSaveService;
import com.project.jobportal.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class JobSeekerSaveController {

    private final UsersService usersService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final JobPostActivityService jobPostActivityService;

    @Autowired
    public JobSeekerSaveController(JobSeekerSaveService jobSeekerSaveService, JobPostActivityService jobPostActivityService,
                                   JobSeekerProfileService jobSeekerProfileService, UsersService usersService) {
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerProfileService=jobSeekerProfileService;
        this.usersService=usersService;
    }

    @PostMapping("job-details/save/{id}")
    public String savedjob(@PathVariable("id") int id,JobSeekerSave jobSeekerSave){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentuser=authentication.getName();
            Users users=usersService.getuserbyemail(currentuser).orElseThrow(()->new UsernameNotFoundException("user not found"));
            Optional<JobSeekerProfile> jobSeekerProfile=jobSeekerProfileService.getUserId(users.getUserId());
            JobPostActivity jobPostActivity=jobPostActivityService.getone(id);
            if(jobSeekerProfile.isPresent() && jobPostActivity !=null){
                jobSeekerSave.setJob(jobPostActivity);
                jobSeekerSave.setUserId(jobSeekerProfile.get());
            }
            else {
                throw new RuntimeException("user not found");
            }
            jobSeekerSaveService.addNew(jobSeekerSave);

        }
        return "redirect:/dashboard/";
    }

    @GetMapping("/saved-jobs/")
    public String viewsavedjob(Model model){

        List<JobPostActivity> jobPostActivity=new ArrayList<>();
        Object user=usersService.getCurrentUserProfile();

        List<JobSeekerSave> saveList=jobSeekerSaveService.getCandidatejob((JobSeekerProfile) user);

        for(JobSeekerSave jobSeekerSave: saveList){
            jobPostActivity.add(jobSeekerSave.getJob());
        }
        model.addAttribute("jobPost",jobPostActivity);
        model.addAttribute("user",user);
        return "saved-jobs";
    }
}
