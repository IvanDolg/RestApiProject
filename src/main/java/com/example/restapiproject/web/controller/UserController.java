package com.example.restapiproject.web.controller;

import com.example.restapiproject.configuration.JWTTokenProvider;
import com.example.restapiproject.configuration.UserPrincipal;
import com.example.restapiproject.dto.UserDTO.LoginUserDto;
import com.example.restapiproject.entity.Role;
import com.example.restapiproject.entity.User;
import com.example.restapiproject.mapper.UserMapper;
import com.example.restapiproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PostMapping("/registration")
    public ResponseEntity<User> registration(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginUserDto dto) {
        UserPrincipal userPrincipal = (UserPrincipal) userService.loadUserByUsername(dto.getUsername());

        if (passwordEncoder.matches(dto.getPassword(), userPrincipal.getPassword())) {
            Set<Role> authorities = (Set<Role>) userPrincipal.getAuthorities();
            LocalDateTime time = LocalDateTime.now();
            dto.setLastVisitDate(time);
            userService.updateLastVisitDate(dto.getUsername(), time);
            userMapper.toUser(dto);
            String token = jwtTokenProvider.generateToken(userPrincipal.getUsername(), userPrincipal.getPassword(), authorities);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.badRequest().build();
    }
}
