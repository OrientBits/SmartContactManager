package com.smart.controller;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {

    private User user;
    int userId;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    //run for every handler
    @ModelAttribute
    public void addCommonData(Model model,Principal principal){
        String name = principal.getName();
        System.out.println("UserName "+name);
        //get the user by username
        user = userRepository.getUserByUserName(name);
        model.addAttribute("user",user);
    }

    //home page
    @RequestMapping("index")
    public String dashboard(Model model, Principal principal){
        model.addAttribute("title","Home");
        System.out.println("Logged in");
        userId = user.getId() + user.getContacts().size();;
        return "normal/user_dashboard";
    }


    //open add form handler
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model){
        model.addAttribute("title","Add Contact");
        model.addAttribute("contact",new Contact());

        return "normal/add_contact_form";
    }


    //processing add contact form
    @PostMapping("/process-contact")
    public String processContact(@Valid @ModelAttribute Contact contact, BindingResult bindingResult,
                                 Model model, HttpSession session,
                                 @RequestParam("profileImage") MultipartFile file){
        try{

            if (bindingResult.hasErrors()){
                System.out.println("Errors: "+ bindingResult);
                throw new Exception("" + bindingResult.getAllErrors());
            }

            //processing file
            if (file.isEmpty()){
                System.out.println("file is empty");
            }else{
                String originalFilename = file.getOriginalFilename();
                originalFilename = originalFilename.substring(0,originalFilename.length()-4)+ userId++ + originalFilename.substring(originalFilename.length()-4);

                contact.setImageUrl(originalFilename);
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsoluteFile() + File.separator + originalFilename);
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Path: "+path);
                System.out.println("Saved successfully!");

            }


            contact.setUser(user);
            user.getContacts().add(contact);
            userRepository.save(user);

            session.setAttribute("message",new Message("Successfully Added!","alert-success"));
            return "normal/add_contact_form";
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            session.setAttribute("message", new Message("Failed to Add Contact! " + e.getMessage(), "alert-danger"));
            return "normal/add_contact_form";
        }
    }


    @GetMapping("/show-contacts")
    public String showContacts(Model model){
        model.addAttribute("title","Show User Contacts");

        /*//first way to get list of contacts
        List<Contact> contacts = user.getContacts();
        model.addAttribute("contacts",contacts);*/

        //by creating contact repository
        List<Contact> contacts = contactRepository.findContactsByUser(user.getId());
        model.addAttribute("contacts", contacts);

        System.out.println("UserId: "+user.getId());
        System.out.println("Contacts: "+contacts);

        return "normal/show_contact";
    }


}
