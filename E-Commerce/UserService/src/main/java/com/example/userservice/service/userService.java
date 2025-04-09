package com.example.userservice.service;

import com.example.userservice.Entity.Dto.userLoginDto;
import com.example.userservice.Entity.Vo.userLoginVo;
import com.example.userservice.Entity.Vo.userRegisterVo;

/**
 * @Author: LH
 * @Date: 2025/3/14 15:43
 */
public interface userService  {

    userLoginVo Login(userLoginDto userLoginDto);

    userRegisterVo Register(userLoginDto userLoginDto);
}
