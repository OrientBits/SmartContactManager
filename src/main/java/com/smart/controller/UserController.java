package com.smart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("user")
public class UserController {

    @RequestMapping("index")
    public String dashboard(Model model, Principal principal){

        return "normal/user_dashboard";

    }

}
