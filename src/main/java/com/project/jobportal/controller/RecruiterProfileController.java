package com.project.jobportal.controller;

import com.project.jobportal.Repository.Usersrepository;
import com.project.jobportal.entity.RecruiterProfile;
import com.project.jobportal.entity.Users;
import com.project.jobportal.service.RecruiterProfileService;
import com.project.jobportal.util.FileuploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {
    private final Usersrepository usersrepository;
    private final RecruiterProfileService recruiterProfileService;

    @Autowired
    public RecruiterProfileController(RecruiterProfileService recruiterProfileService,Usersrepository usersrepository) {
        this.recruiterProfileService = recruiterProfileService;
        this.usersrepository=usersrepository;
    }

    @GetMapping("/")
    public String addprofile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentusername= authentication.getName();
            Users users=usersrepository.findByEmail(currentusername).orElseThrow(()->new UsernameNotFoundException("could not"+"found user"));
            Optional<RecruiterProfile> recruiterProfile=recruiterProfileService.getUserID(users.getUserId());

            if(!recruiterProfile.isEmpty()){
                model.addAttribute("profile",recruiterProfile.get());
            }

        }
        return "recruiter-profile";
    }

    @PostMapping("/addNew")
    public String addnewprofile(RecruiterProfile recruiterProfile, @RequestParam("image") MultipartFile multipartFile, Model model){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentusername=authentication.getName();
            Users users=usersrepository.findByEmail(currentusername).orElseThrow(()-> new UsernameNotFoundException("could not"+"found user."));
            recruiterProfile.setUsersId(users);
            recruiterProfile.setUserAccountId(users.getUserId());
        }
        model.addAttribute("profile",recruiterProfile);
        String filename="";
        if(!multipartFile.getOriginalFilename().equals("")){
            filename= StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            recruiterProfile.setProfilePhoto(filename);
        }
        RecruiterProfile saveduser=recruiterProfileService.addnew(recruiterProfile);
        String uploaddir="photos/recruiter/"+saveduser.getUserAccountId();

        try{
            FileuploadUtil.savefile(uploaddir,filename,multipartFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:/dashboard/";
    }
}
