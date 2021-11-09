package com.example.smartcontactmanager.Entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Size(min = 2,max = 20,message = "name must be between 2 to 20 charactor")
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String role;
    @Column(length = 500)
    private String about;
    private boolean enabled;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    private List<Contact> contacts= new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\''  +
                ", about='" + about + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
