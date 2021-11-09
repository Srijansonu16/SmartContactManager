package com.example.smartcontactmanager.Config;

import com.example.smartcontactmanager.Dao.UserRepository;
import com.example.smartcontactmanager.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpal implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user1 = userRepository.getUserByUserName(s);
        if(user1==null){
            throw new UsernameNotFoundException("user not found");
        }
        CustomUserdDetail customUserdDetail = new CustomUserdDetail(user1);

        return customUserdDetail;
    }
}
