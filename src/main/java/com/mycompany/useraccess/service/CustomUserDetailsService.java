package com.mycompany.useraccess.service;


import com.mycompany.useraccess.model.User;
import com.mycompany.useraccess.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;




    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(usernameOrEmail)
                                  .or(() -> userRepository.findByEmail(usernameOrEmail))
                                  .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));

        return org.springframework.security.core.userdetails.User.builder()
                                                                 .username(user.getEmail()) // or user.getUsername()
                                                                 .password(user.getPassword())
                                                                 .roles(user.getRole().name())
                                                                 .disabled(!user.isEnabled())
                                                                 .build();
    }


}