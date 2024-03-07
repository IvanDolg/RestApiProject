package com.example.restapiproject.service.impl;

import com.example.restapiproject.configuration.UserPrincipal;
import com.example.restapiproject.entity.Role;
import com.example.restapiproject.entity.User;
import com.example.restapiproject.repository.UserRepository;
import com.example.restapiproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${app.user.check.days}")
    private int userCheckDays;
    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.USER);
        user.setLastVisitDate(LocalDateTime.now());
        return userRepository.save(user);

    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow();

        UserPrincipal userPrincipal = UserPrincipal.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();

        return userPrincipal;
    }

    @Override
    public void updateLastVisitDate(String username, LocalDateTime lastVisitDate) {
        userRepository.updateLastVisitDate(username, lastVisitDate);
    }

    @Override
    public void checkUserDate() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            for (User user : users) {
                LocalDateTime lastVisitDate = user.getLastVisitDate();
                if (lastVisitDate.isBefore(currentDateTime.minusDays(userCheckDays))) {
                    userRepository.delete(user);
                }
            }
        }
    }
}
