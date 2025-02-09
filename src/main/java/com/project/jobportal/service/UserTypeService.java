package com.project.jobportal.service;

import com.project.jobportal.Repository.UserTypeRepository;
import com.project.jobportal.entity.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeService {

    private final UserTypeRepository userTypeRepository;


    @Autowired
    public UserTypeService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    public List<UserType> getall(){
        return userTypeRepository.findAll();
    }

}
