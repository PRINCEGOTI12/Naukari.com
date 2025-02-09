package com.project.jobportal.controller;

import com.project.jobportal.entity.UserType;
import com.project.jobportal.entity.Users;
import com.project.jobportal.service.UserTypeService;
import com.project.jobportal.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;
import java.util.List;

@Controller
public class Usercontroller {

    private final UserTypeService userTypeService;
    private final UsersService usersService;

    @Autowired
    public Usercontroller(UserTypeService userTypeService, UsersService usersService) {
        this.usersService=usersService;
        this.userTypeService = userTypeService;
    }

    @GetMapping("/register")
    public String getall(Model model){
        List<UserType> userTypes=userTypeService.getall();
        model.addAttribute("getAllTypes", userTypes);
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/register/new")
    public String userregisteration(@Validated Users users,  Model model){

        Optional<Users> optionalUsers = usersService.getuserbyemail(users.getEmail());
        if(optionalUsers.isPresent())
        {
            model.addAttribute("error", "Email has already registered. Please login.");
            List<UserType> userTypes=userTypeService.getall();
            model.addAttribute("getAllTypes", userTypes);
            model.addAttribute("user", new Users());
            return "register";
        }

        usersService.addnew(users);
        return "redirect:/dashboard/";

    }

    @GetMapping("/login")
    public String loginpage(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication !=null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return "redirect:/";
    }
}
