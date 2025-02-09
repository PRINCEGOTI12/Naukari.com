package com.project.jobportal.service;

import com.project.jobportal.Repository.JobSeekerProfileRepository;
import com.project.jobportal.Repository.Usersrepository;
import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.entity.RecruiterProfile;
import com.project.jobportal.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final Usersrepository usersrepository;

    @Autowired
    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, Usersrepository usersrepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.usersrepository = usersrepository;
    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile){
        return jobSeekerProfileRepository.save(jobSeekerProfile);
    }


    public Optional<JobSeekerProfile> getUserId(Integer userId) {
        return jobSeekerProfileRepository.findById(userId);
    }

    public JobSeekerProfile getCurrentJobSeekerProfile() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentusername=authentication.getName();
            Users users=usersrepository.findByEmail(currentusername).orElseThrow(()-> new UsernameNotFoundException("user could not found"));
            Optional<JobSeekerProfile> jobSeekerProfile=getUserId(users.getUserId());
            return jobSeekerProfile.orElse(null);
        }else return null;
    }
}
