package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository repository;

    @RequestMapping("index")
    public String dashboard(Model model, Principal principal){

        String name = principal.getName();
        System.out.println("USERNAME "+name);

        //get the user by username
        User user = repository.getUserByUserName(name);

        model.addAttribute("user",user);

        return "normal/user_dashboard";

    }

    //open add form handler

    @GetMapping("/add-contact")
    public String openAddContactForm(Model model){
        model.addAttribute("title","Add Contact");
        //model.addAttribute("contact",new Contact());

        return "normal/add_contact_form";
    }



    @ModelAttribute
    public void addCommonData(Model model,Principal principal){
        String name = principal.getName();
        System.out.println("USERNAME "+name);
        //get the user by username
        User user = repository.getUserByUserName(name);
        model.addAttribute("user",user);
    }


}
