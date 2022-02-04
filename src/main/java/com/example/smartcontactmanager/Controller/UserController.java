package com.example.smartcontactmanager.Controller;

import com.example.smartcontactmanager.Dao.ContactRepository;
import com.example.smartcontactmanager.Dao.UserRepository;
import com.example.smartcontactmanager.Entity.Contact;
import com.example.smartcontactmanager.Entity.User;
import com.example.smartcontactmanager.Message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public ContactRepository contactRepository;

    @ModelAttribute
    public void Common(Model model,Principal principal){
        String name = principal.getName();
        System.out.println(name);
        User user = userRepository.getUserByUserName(name);

        model.addAttribute("user",user);
    }



    @RequestMapping("/index")
    public String Dashboard(@ModelAttribute("user") User user, Model model, Principal principal)throws NullPointerException{
        model.addAttribute("title", userRepository.getUserByUserName(principal.getName()).getName());
        String name = user.getName();
        if (name!=null) {
            System.out.println(name);
            return "NormalUser/index";
        }else {
            System.out.println("no name");
            return "redirect:/logout";
        }

    }


    @GetMapping("/addcontact")
    public String AddContact(Model model,Principal principal){
        model.addAttribute("title","Add_Contacts");
        model.addAttribute("contact", new Contact());

        return "NormalUser/addcontact";
    }
    @GetMapping("/viewcontact/{page}")
    public String viewContact(@PathVariable("page") Integer page, Model model,Principal principal){
        model.addAttribute("title","View_Contacts");


        String name = principal.getName();
        User user = this.userRepository.getUserByUserName(name);
        PageRequest pageRequest = PageRequest.of(page, 3);

        Page<Contact> contacts = this.contactRepository.findByUserId(user.getId(),pageRequest);
        System.out.println(contacts);
        model.addAttribute("contact" , contacts);
        model.addAttribute("currentpage",page);
        model.addAttribute("totalpage",contacts.getTotalPages());

        return "NormalUser/viewcontact";
    }



    @PostMapping(value = "/save_contact")
    public String SaveContact(@Valid  @ModelAttribute("contact") Contact contact
            ,BindingResult result, Principal principal
    , HttpSession session) throws IOException , MultipartException {

      try {
          String name = principal.getName();
          User userByUserName = this.userRepository.getUserByUserName(name);
          userByUserName.getContacts().add(contact);
          contact.setUser(userByUserName);
          this.userRepository.save(userByUserName);
          System.out.println(contact);
          session.setAttribute("message",new Message("Contact Added!!","alert-success"));
          return "NormalUser/addcontact";
      }catch (Exception e){
          e.printStackTrace();
          session.setAttribute("message",new Message("Something went wrong!!!","alert-danger"));

          return "NormalUser/addcontact";
      }

    }
    @GetMapping("/Delete/{id}")
    public String Delete(Model model, @PathVariable("id") Integer id){
        Optional<Contact> byId = this.contactRepository.findById(id);
        Contact contact = byId.get();
        contact.setUser(null);
        this.contactRepository.delete(contact);
        System.out.println("contact Deleted");
        return "redirect:/user/viewcontact/0";
    }
     @GetMapping("/contact/{id}")
    public String ContactDetails(@PathVariable("id") Integer id,Model model,Principal principal){
         String name = principal.getName();
         User user = this.userRepository.getUserByUserName(name);

         Optional<Contact> contactOptional = this.contactRepository.findById(id);
         Contact contact = contactOptional.get();
         model.addAttribute("title",contact.getName());

         if(user.getId()==contact.getUser().getId())
             model.addAttribute("contact",contact);

         return "NormalUser/contactdetail";
    }


    // open update form handler
    @GetMapping("/update/{id}")
    public String UpdateForm(@PathVariable("id") Integer id,Model model){
        Optional<Contact> byId = this.contactRepository.findById(id);
        Contact contact = byId.get();
        model.addAttribute("contact",contact);

        return "NormalUser/update";

    }
    @PostMapping("/Update_contact")
    public  String updateContact(@ModelAttribute("contact") Contact contact,Model model,Principal principal){
        String name = principal.getName();
        User user = userRepository.getUserByUserName(name);

        user.getContacts().add(contact);
        contact.setUser(user);
        this.userRepository.save(user);

//        this.contactRepository.save(contact);
        return "redirect:/user/viewcontact/0";
    }

    // profile handler
    @GetMapping("/profile")
    public String Profile(){
        return "NormalUser/Profile";
    }

// delete user
    @GetMapping("/deleteuser")
    public String DeleteUser(Principal principal){
        String name = principal.getName();
        User user = userRepository.getUserByUserName(name);
        this.userRepository.delete(user);

        return "redirect:/logout";
    }

// update user
    @GetMapping("/update_user/{id}")
    public String UpdateUser(@PathVariable("id") Integer id,Model model){
        Optional<User> byId = this.userRepository.findById(id);
        User user = byId.get();
        model.addAttribute("user" ,user);

        return "NormalUser/UpdateUser";
    }

    // DoUpdate User
    @PostMapping("/DoUser_Update")
    public String updatingUser (@Valid Principal principal,@ModelAttribute("user") User user ,BindingResult result){
        String name = principal.getName();
        User user1 = this.userRepository.getUserByUserName(name);
        this.userRepository.save(user1);


        return "redirect:/user/profile";
    }
    @GetMapping("/settings")
    public String Settings(){
        return "NormalUser/Settings";
    }
    @PostMapping("/passwordchange")
    public String passwordChange( @Valid @ModelAttribute("user") User user,Principal principal,@RequestParam("password1") String password1,@RequestParam("password2") String password2, BindingResult result,HttpSession session){
        String name = principal.getName();
        User user1 = this.userRepository.getUserByUserName(name);
        if(this.bCryptPasswordEncoder.matches(password1,user1.getPassword())){
          user1.setPassword(this.bCryptPasswordEncoder.encode(password2));
          this.userRepository.save(user1);
            session.setAttribute("message",new Message("password changed successfully","alert-success"));

            return "redirect:/user/profile";
        }else {
            session.setAttribute("message",new Message("please write correct old password","alert-warning"));

            return "redirect:/user/settings";
        }


    }
}
