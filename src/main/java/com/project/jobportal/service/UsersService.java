package com.project.jobportal.service;

import com.project.jobportal.Repository.JobSeekerProfileRepository;
import com.project.jobportal.Repository.RecruiterProfileRepository;
import com.project.jobportal.Repository.Usersrepository;
import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.entity.RecruiterProfile;
import com.project.jobportal.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class UsersService {

    private final Usersrepository usersrepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(Usersrepository usersrepository,JobSeekerProfileRepository jobSeekerProfileRepository,
                        RecruiterProfileRepository recruiterProfileRepository,PasswordEncoder passwordEncoder){
        this.usersrepository=usersrepository;
        this.jobSeekerProfileRepository=jobSeekerProfileRepository;
        this.recruiterProfileRepository=recruiterProfileRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public Users addnew(Users users){
        users.setActive(true);
        users.setRegistration_date(new Date(System.currentTimeMillis()));
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users saveduser=usersrepository.save(users);
        int userTypeId=users.getUserTypeId().getUserTypeId();

        if(userTypeId==1){
            recruiterProfileRepository.save(new RecruiterProfile(saveduser));
        }
        else {
            jobSeekerProfileRepository.save(new JobSeekerProfile(saveduser));
        }

        return saveduser;
    }

    public Optional<Users> getuserbyemail(String email){
        return usersrepository.findByEmail(email);
    }

    public Object getCurrentUserProfile() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String username=authentication.getName();
            Users users=usersrepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("could not found.." +"user"));
            int userid=users.getUserId();
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                RecruiterProfile recruiterProfile=recruiterProfileRepository.findById(userid).orElse(new RecruiterProfile());
                return  recruiterProfile;
            }
            else{
                JobSeekerProfile jobSeekerProfile=jobSeekerProfileRepository.findById(userid).orElse(new JobSeekerProfile());
                return jobSeekerProfile;
            }

        }
        return null;
    }
     public Users getcurrentuser(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken))
        {
            String username=authentication.getName();
            Users user=usersrepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("could not"+"found user."));
            return user;
        }
        return null;

     }
}
