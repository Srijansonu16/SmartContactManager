package com.example.smartcontactmanager.Controller;

import com.example.smartcontactmanager.Dao.ContactRepository;
import com.example.smartcontactmanager.Dao.UserRepository;
import com.example.smartcontactmanager.Entity.Contact;
import com.example.smartcontactmanager.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchBar {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;


    @GetMapping("/Search_bar/{query}")
    public ResponseEntity<?> Search(Principal principal, @PathVariable("query") String query){
        User user = this.userRepository.getUserByUserName(principal.getName());

        List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);

        System.out.println("running");
        return ResponseEntity.ok(contacts);
    }
}
