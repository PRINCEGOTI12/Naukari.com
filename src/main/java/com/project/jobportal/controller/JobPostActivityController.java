package com.project.jobportal.controller;

import com.project.jobportal.entity.*;
import com.project.jobportal.service.JobPostActivityService;
import com.project.jobportal.service.JobSeekerApplyService;
import com.project.jobportal.service.JobSeekerSaveService;
import com.project.jobportal.service.UsersService;
import org.hibernate.type.format.jackson.JacksonJsonFormatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class JobPostActivityController {
    private final UsersService usersService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    @Autowired
    public JobPostActivityController(UsersService usersService, JobPostActivityService jobPostActivityService,
                                     JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @GetMapping("/dashboard/")
    public String searchjob(Model model,
                            @RequestParam(value = "job",required = false) String job,
                            @RequestParam(value = "location",required = false) String location,
                            @RequestParam(value = "partTime",required = false) String partTime,
                            @RequestParam(value = "fullTime",required = false) String fullTime,
                            @RequestParam(value = "freelance",required = false) String freelance,
                            @RequestParam(value = "internship",required = false) String internship,
                            @RequestParam(value = "remote",required = false) String remote,
                            @RequestParam(value = "onsite",required = false) String onsite,
                            @RequestParam(value = "hybrid",required = false) String hybrid,
                            @RequestParam(value = "today",required = false) boolean today,
                            @RequestParam(value = "days7",required = false) boolean days7,
                            @RequestParam(value = "days30",required = false) boolean days30)

    {
        model.addAttribute("partTime", Objects.equals(partTime,"Part-Time"));
        model.addAttribute("fullTime", Objects.equals(fullTime,"Full-Time"));
        model.addAttribute("freelance", Objects.equals(freelance,"Freelance"));
        model.addAttribute("internship", Objects.equals(internship,"Internship"));

        model.addAttribute("remote", Objects.equals(remote,"Remote"));
        model.addAttribute("onsite", Objects.equals(onsite,"Onsite"));
        model.addAttribute("hybrid", Objects.equals(hybrid,"Hybrid"));

        model.addAttribute("today",today);
        model.addAttribute("days7",days7);
        model.addAttribute("days30",days30);

        model.addAttribute("job",job);
        model.addAttribute("location",location);

        LocalDate searchdate=null;
        List<JobPostActivity> jobPost=null;
        boolean dateSearchflag=true;
        boolean remotetype =true;
        boolean type=true;

        if(days30){
            searchdate=LocalDate.now().minusDays(30);
        } else if (days7) {
            searchdate=LocalDate.now().minusDays(7);
        } else if (today) {
            searchdate=LocalDate.now();
        } else{
            dateSearchflag=false;
        }

        if(partTime==null && fullTime==null && freelance==null && internship==null){
            partTime="Part-Time";
            fullTime="Full-Time";
            freelance="Freelance";
            internship="Internship";
            remotetype=false;
        }

        if(remote==null && onsite==null && hybrid==null){
            remote="Remote";
            onsite="Onsite";
            hybrid="Hybrid";
            type=false;
        }

        if(!dateSearchflag && !remotetype && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)){
            jobPost=jobPostActivityService.getAll();
        }
        else {
            jobPost=jobPostActivityService.search(job,location, Arrays.asList(partTime,fullTime,freelance,internship),Arrays.asList(remote,onsite,hybrid),searchdate);

        }

        Object currentuserprofile=usersService.getCurrentUserProfile();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String username=authentication.getName();
            model.addAttribute("username",username);
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                List<RecruiterjobsDto> recruiterjobsDtos=jobPostActivityService.getRecruiterjob(((RecruiterProfile) currentuserprofile).getUserAccountId());
                model.addAttribute("jobPost",recruiterjobsDtos);
            }
            else {
                List<JobSeekerApply> applyList=jobSeekerApplyService.getCandidatejob((JobSeekerProfile) currentuserprofile);
                List<JobSeekerSave> saveList=jobSeekerSaveService.getCandidatejob((JobSeekerProfile) currentuserprofile);

                boolean exist;
                boolean saved;

                for(JobPostActivity jobPostActivity: jobPost){
                    exist=false;
                    saved=false;
                    for(JobSeekerApply jobSeekerApply: applyList){
                        if(Objects.equals(jobPostActivity.getJobPostId(),jobSeekerApply.getJob().getJobPostId())){
                            jobPostActivity.setIsActive(true);
                            exist=true;
                            break;
                        }
                    }
                    for(JobSeekerSave jobSeekerSave:saveList){
                        if(Objects.equals(jobPostActivity.getJobPostId(),jobSeekerSave.getJob().getJobPostId())){
                            jobPostActivity.setIsSaved(true);
                            saved=true;
                            break;

                        }
                    }
                    if(!exist){
                        jobPostActivity.setIsActive(false);
                    }
                    if(!saved){
                        jobPostActivity.setIsSaved(false);
                    }
                    model.addAttribute("jobPost",jobPost);
                }

            }
        }
        model.addAttribute("user",currentuserprofile);
        return "dashboard";
    }

    @GetMapping("/global-search/")
    public String globalSearch(Model model,
                               @RequestParam(value = "job",required = false) String job,
                               @RequestParam(value = "location",required = false) String location,
                               @RequestParam(value = "partTime",required = false) String partTime,
                               @RequestParam(value = "fullTime",required = false) String fullTime,
                               @RequestParam(value = "freelance",required = false) String freelance,
                               @RequestParam(value = "internship",required = false) String internship,
                               @RequestParam(value = "remote",required = false) String remote,
                               @RequestParam(value = "onsite",required = false) String onsite,
                               @RequestParam(value = "hybrid",required = false) String hybrid,
                               @RequestParam(value = "today",required = false) boolean today,
                               @RequestParam(value = "days7",required = false) boolean days7,
                               @RequestParam(value = "days30",required = false) boolean days30){

        model.addAttribute("partTime", Objects.equals(partTime,"Part-Time"));
        model.addAttribute("fullTime", Objects.equals(fullTime,"Full-Time"));
        model.addAttribute("freelance", Objects.equals(freelance,"Freelance"));
        model.addAttribute("internship", Objects.equals(internship,"Internship"));

        model.addAttribute("remote", Objects.equals(remote,"Remote"));
        model.addAttribute("onsite", Objects.equals(onsite,"Onsite"));
        model.addAttribute("hybrid", Objects.equals(hybrid,"Hybrid"));

        model.addAttribute("today",today);
        model.addAttribute("days7",days7);
        model.addAttribute("days30",days30);

        model.addAttribute("job",job);
        model.addAttribute("location",location);

        LocalDate searchdate=null;
        List<JobPostActivity> jobPost=null;
        boolean dateSearchflag=true;
        boolean remotetype =true;
        boolean type=true;

        if(days30){
            searchdate=LocalDate.now().minusDays(30);
        } else if (days7) {
            searchdate=LocalDate.now().minusDays(7);
        } else if (today) {
            searchdate=LocalDate.now();
        } else{
            dateSearchflag=false;
        }

        if(partTime==null && fullTime==null && freelance==null && internship==null){
            partTime="Part-Time";
            fullTime="Full-Time";
            freelance="Freelance";
            internship="Internship";
            remotetype=false;
        }

        if(remote==null && onsite==null && hybrid==null){
            remote="Remote";
            onsite="Onsite";
            hybrid="Hybrid";
            type=false;
        }

        if(!dateSearchflag && !remotetype && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)){
            jobPost=jobPostActivityService.getAll();
        }
        else {
            jobPost=jobPostActivityService.search(job,location, Arrays.asList(partTime,fullTime,freelance,internship),Arrays.asList(remote,onsite,hybrid),searchdate);

        }
        model.addAttribute("jobPost",jobPost);
        return "global-search";

    }

    @GetMapping("/dashboard/add")
    public String addNewPost(Model model){
        model.addAttribute("jobPostActivity",new JobPostActivity());
        model.addAttribute("user",usersService.getCurrentUserProfile());
        return "add-jobs";
    }

    @PostMapping("/dashboard/addNew")
    public String savepost(JobPostActivity jobPostActivity,Model model){
        Users user=usersService.getcurrentuser();
        if(user!=null){
            jobPostActivity.setPostedById(user);
        }
        jobPostActivity.setPostedDate(new Date());
        model.addAttribute("jobPostActivity",jobPostActivity);
        JobPostActivity saved=jobPostActivityService.addNew(jobPostActivity);
        return "redirect:/dashboard/";
    }

    @PostMapping("/dashboard/edit/{id}")
    public String edit(@PathVariable("id")int id, Model model){
        JobPostActivity jobPostActivity=jobPostActivityService.getone(id);
        model.addAttribute("jobPostActivity",jobPostActivity);
        model.addAttribute("user",usersService.getCurrentUserProfile());
        return "add-jobs";
    }

    @PostMapping("/dashboard/deleteJob/{id}")
    public String delete(@PathVariable("id") int id,Model model){

        return "redirect:/dashboard/";
    }
}