package com.project.jobportal.service;

import com.project.jobportal.Repository.Usersrepository;
import com.project.jobportal.entity.Users;
import com.project.jobportal.util.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private  final Usersrepository usersrepository;

    @Autowired
    public CustomUserDetailService(Usersrepository usersrepository) {
        this.usersrepository = usersrepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user=usersrepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("could not found."));
        return new CustomUserDetails(user);
    }
}
