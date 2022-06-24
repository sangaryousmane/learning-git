package com.shopme.admin.security;

import com.shopme.admin.user.repository.UserRepository;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class ShopmeUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    public ShopmeUserDetailsService(){}


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User getUserByEmail=userRepository.getUserByEmail(email);

        if (!(getUserByEmail ==null)){
            return new ShopmeUserDetails(getUserByEmail);
        }
        throw new UsernameNotFoundException("Couldn't find user with the email: " + email);
    }
}
