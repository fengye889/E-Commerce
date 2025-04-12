package com.example.userservice.service;

import com.example.userservice.Entity.Dto.userLoginDto;
import com.example.userservice.Entity.Vo.userLoginVo;
import com.example.userservice.Entity.Vo.userRegisterVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @Author: LH
 * @Date: 2025/3/14 15:43
 */
public interface userService  {

    userLoginVo login(userLoginDto userLoginDto, HttpServletResponse response);

    userRegisterVo register(userLoginDto userLoginDto);
}
