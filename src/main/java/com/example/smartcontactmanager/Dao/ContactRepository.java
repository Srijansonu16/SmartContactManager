package com.example.smartcontactmanager.Dao;

import com.example.smartcontactmanager.Entity.Contact;
import com.example.smartcontactmanager.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,Integer> {
    @Query("from Contact as c where c.user.id=:UserId")
    public Page<Contact> findByUserId(Integer UserId , Pageable pageable);

    public List<Contact> findByNameContainingAndUser (String name , User user);
}
