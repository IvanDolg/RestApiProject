package com.example.restapiproject.mapper;

import com.example.restapiproject.dto.UserDTO.LoginUserDto;
import com.example.restapiproject.entity.User;

public interface GeneralMapper {
    User mapToUser(LoginUserDto loginUserDto);
    LoginUserDto mapToLoginUserDto(User user);
}
