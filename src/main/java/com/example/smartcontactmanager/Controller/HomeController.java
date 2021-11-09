package com.example.smartcontactmanager.Controller;

import com.example.smartcontactmanager.Dao.UserRepository;
import com.example.smartcontactmanager.Entity.User;
import com.example.smartcontactmanager.Message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public UserRepository userRepository;

    @GetMapping("/home")
    public String Home(Model model){
        System.out.println("home..........");
        model.addAttribute("title" , "Home_ContactController");

        return "Home";
    }
    @GetMapping("/about")
    public String About(Model model){
        System.out.println("about......");
        model.addAttribute("title" , "About_ContactController");

        return "about";
    }
    @GetMapping("/signup")
    public String Signup(Model model){
        System.out.println("signup.....");
        model.addAttribute("user",new User());
        model.addAttribute("title" , "Signup_ContactController");

        return "signup";
    }
    @GetMapping("/login")
    public String login(Model model){
        System.out.println("login.....");
        model.addAttribute("title" , "LogIn_ContactController");

        return "login";
    }

    // handler for registering user
    @PostMapping("/do_register" )
    public String Registering( @Valid @ModelAttribute("user") User user,BindingResult result,
                              @RequestParam (value ="check",defaultValue = "false")
                                      boolean check, Model model , HttpSession session){


        try {

            user.setRole("ROLE_USER");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            this.userRepository.save(user);
            System.out.println(check);
            System.out.println(user);
            session.setAttribute("message",new Message("Successful registered","alert-success"));
            return "signup";

        }catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message",new Message("Something went wrong","alert-warning"));
            return "signup";
        }

    }

}
