package com.example.restapiproject.service;

import com.example.restapiproject.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public interface UserService {
    public User save(User user);
    public UserDetails loadUserByUsername(String username);
    public void updateLastVisitDate(String username, LocalDateTime lastVisitDate);
}
