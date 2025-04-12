package com.example.userservice.controller;

import com.example.userservice.Entity.Dto.userLoginDto;
import com.example.userservice.Entity.User;
import com.example.userservice.Entity.Vo.userLoginVo;
import com.example.userservice.Entity.Vo.userRegisterVo;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.service.userService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: LH
 * @Date: 2025/3/14 15:35
 */
@RestController
@RequestMapping("/user")
public class userController {
    @Autowired
    userService  userService;
    @Autowired
    UserMapper userMapper;
    @PostMapping("/login")
    public userLoginVo Login(@RequestBody userLoginDto userLoginDto, HttpServletResponse response){
        return userService.login(userLoginDto,response);
    }
    @PostMapping("/register")
    public userRegisterVo register(@RequestBody userLoginDto userLoginDto ){
         return userService.register(userLoginDto);
    }
    @GetMapping("/{id}")
    public User selectById(@PathVariable("id") Long id){
        return  userMapper.selectById(id);
    }
}