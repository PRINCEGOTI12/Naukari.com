package com.project.jobportal.service;

import com.project.jobportal.Repository.RecruiterProfileRepository;
import com.project.jobportal.Repository.Usersrepository;
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
public class RecruiterProfileService {
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final Usersrepository usersrepository;


    @Autowired
    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository
                                    ,Usersrepository usersrepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.usersrepository=usersrepository;
    }

    public RecruiterProfile addnew(RecruiterProfile recruiterProfile){
        return recruiterProfileRepository.save(recruiterProfile);
    }

    public Optional<RecruiterProfile> getUserID(int id){
        return recruiterProfileRepository.findById(id);
    }

    public RecruiterProfile getCurrentRecruiterProfile() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentusername=authentication.getName();
            Users users=usersrepository.findByEmail(currentusername).orElseThrow(()-> new UsernameNotFoundException("user could not found"));
            Optional<RecruiterProfile> recruiterProfile=getUserID(users.getUserId());
            return recruiterProfile.orElse(null);
        }else return null;
    }
}
