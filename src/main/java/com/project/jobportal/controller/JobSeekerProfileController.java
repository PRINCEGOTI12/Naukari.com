package com.project.jobportal.controller;

import com.project.jobportal.Repository.Usersrepository;
import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.entity.Skill;
import com.project.jobportal.entity.Users;
import com.project.jobportal.service.JobSeekerProfileService;
import com.project.jobportal.util.FileDownloadUtil;
import com.project.jobportal.util.FileuploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {
    private final JobSeekerProfileService jobSeekerProfileService;
    private final Usersrepository usersrepository;

    @Autowired
    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, Usersrepository usersrepository) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.usersrepository = usersrepository;
    }

    @GetMapping("/")
    public String addProfile(Model model){
        JobSeekerProfile jobSeekerProfile=new JobSeekerProfile();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        List<Skill> skills=new ArrayList<>();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users user=usersrepository.findByEmail(authentication.getName()).orElseThrow(()-> new UsernameNotFoundException("user could not found"));
            Optional<JobSeekerProfile> seekerProfile=jobSeekerProfileService.getUserId(user.getUserId());
            if(seekerProfile.isPresent()){
                jobSeekerProfile=seekerProfile.get();
                if(jobSeekerProfile.getSkillList().isEmpty()){
                    skills.add(new Skill());
                    jobSeekerProfile.setSkillList(skills);
                }
            }
            model.addAttribute("skills",skills);
            model.addAttribute("profile",jobSeekerProfile);

        }

        return "jobseeker-profile";
    }

    @PostMapping("/addNew")
    public String addnewprofile(Model model,
                                @RequestParam("image")MultipartFile image,
                                @RequestParam("pdf") MultipartFile pdf,
                                JobSeekerProfile  jobSeekerProfile){

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users users=usersrepository.findByEmail(authentication.getName()).orElseThrow(()->new UsernameNotFoundException("users could not found."));
            jobSeekerProfile.setUserId(users);
            jobSeekerProfile.setUserAccountId(users.getUserId());
        }
        List<Skill> skillList=new ArrayList<>();
        model.addAttribute("profile",jobSeekerProfile);
        model.addAttribute("skills",skillList);

        for(Skill skills:jobSeekerProfile.getSkillList()){
            skills.setJobSeekerProfile(jobSeekerProfile);
        }
        System.out.println("skilllist: "+jobSeekerProfile.getSkillList());
        String imagePath="";
        String pdfPath="";

        if(!Objects.equals(image.getOriginalFilename(),"")){
            imagePath= StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            jobSeekerProfile.setProfilePhoto(imagePath);
        }

        if(!Objects.equals(pdf.getOriginalFilename(),"")){
            pdfPath=StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
            jobSeekerProfile.setResume(pdfPath);
        }
        JobSeekerProfile seekerProfile=jobSeekerProfileService.addNew(jobSeekerProfile);
        String uploaddir="photos/candidate/"+seekerProfile.getUserAccountId();
        try{

            if(!Objects.equals(image.getOriginalFilename(),"")){
                FileuploadUtil.savefile(uploaddir,imagePath,image);
            }
            if(!Objects.equals(pdf.getOriginalFilename(),"")){
                FileuploadUtil.savefile(uploaddir,pdfPath,pdf);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return "redirect:/dashboard/";
    }

    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id") int id, Model model) {

        Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getUserId(id);
        model.addAttribute("profile", seekerProfile.get());
        return "jobseeker-profile";
    }

    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(@RequestParam(value = "fileName") String filename,@RequestParam(value = "userID") String userId){
        FileDownloadUtil fileDownloadUtil=new FileDownloadUtil();

        Resource resource=null;

        try{
            resource=fileDownloadUtil.getfileasresource("photos/candidate/"+userId,filename);
        }
        catch (IOException io){
            return ResponseEntity.badRequest().build();
        }
        if(resource==null){
            return new ResponseEntity<>("file not found", HttpStatus.NOT_FOUND);
        }
        String contentType="application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,headerValue)
                .body(resource);
    }

}
