package com.example.restapiproject.service;

import com.example.restapiproject.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public interface UserService {
    User save(User user);
    UserDetails loadUserByUsername(String username);
    void updateLastVisitDate(String username, LocalDateTime lastVisitDate);
    void checkUserDate();

}
