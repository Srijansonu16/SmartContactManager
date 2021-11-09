package com.example.smartcontactmanager.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Size(min = 2 ,max = 20,message = "name must be between two to twenty character")
    private String name;
    @Column(unique = true)
    private String email;
    private String nickName;
    private String work;
    @Column(length = 500)
    private String about;
    @Column(unique = true)
    @Size(max = 12,message = "contact length exceed 12 digit, or number already exist")
    private String phoneNo;
    @ManyToOne
    @JsonIgnore
    private User user;

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                ", work='" + work + '\'' +
                ", about='" + about + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }
}
