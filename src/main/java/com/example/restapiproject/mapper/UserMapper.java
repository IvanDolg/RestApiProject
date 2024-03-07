package com.example.restapiproject.mapper;

import com.example.restapiproject.dto.UserDTO.LoginUserDto;
import com.example.restapiproject.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static LoginUserDto toLoginUserDto(User user) {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setId(user.getId());
        loginUserDto.setUsername(user.getUsername());
        loginUserDto.setPassword(user.getPassword());
        loginUserDto.setLastVisitDate(user.getLastVisitDate());
        return loginUserDto;
    }

    public static User toUser(LoginUserDto loginUserDto) {
        User user = new User();
        user.setId(loginUserDto.getId());
        user.setUsername(loginUserDto.getUsername());
        user.setPassword(loginUserDto.getPassword());
        user.setLastVisitDate(loginUserDto.getLastVisitDate());
        return user;
    }
}
