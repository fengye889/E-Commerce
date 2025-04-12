package com.example.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.userservice.Entity.Dto.userLoginDto;
import com.example.userservice.Entity.User;
import com.example.userservice.Entity.Vo.userLoginVo;
import com.example.userservice.Entity.Vo.userRegisterVo;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.service.userService;
import com.example.userservice.utils.jwtUtils;
import com.example.userservice.utils.md5;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: LH
 * @Date: 2025/3/14 15:42
 */
@Service
public class userServiceImpl  implements userService {
      @Autowired
      UserMapper userMapper;
    @Override
    public userLoginVo login(userLoginDto userLoginDto, HttpServletResponse response) {
        userLoginVo userLoginVo =new userLoginVo();
        Map<String, Object> claims = new HashMap<>();
        String phone =userLoginDto.getPhone();
        String password=md5.md5Encrypt(userLoginDto.getPassword());
        //  查询账号密码
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,phone);
        User user=userMapper.selectOne(wrapper);
        if(user == null) {
             userLoginVo.setCode("0");
             userLoginVo.setMsg("账号不存在");
             return userLoginVo;
        }
        if (!user.getPassword().equals(password)){
            userLoginVo.setCode("2");
            userLoginVo.setMsg("密码错误");
            return userLoginVo;
        }
        claims.put("id",user.getUserId());
        claims.put("phone",user.getPhone());
        //封装Vo
        userLoginVo.setCode("1");
        userLoginVo.setMsg("登入成功");
        userLoginVo.setUserId(user.getUserId());
        userLoginVo.setPhone(user.getPhone());
        //将token存储到Cookie中，防止直接暴露给前端
        String token = jwtUtils.generateJWT(claims);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);  // 防止通过 JavaScript 访问
        cookie.setSecure(true);    // 仅通过 HTTPS 发送
        cookie.setPath("/");       // 设置 Cookie 可用于所有路径
        cookie.setMaxAge(3600);    // 设置过期时间，单位为秒
        response.addCookie(cookie);
        return userLoginVo;
    }
    @Override
    public userRegisterVo register(userLoginDto userLoginDto) {
        userRegisterVo userRegisterVo = new userRegisterVo();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,userLoginDto.getPhone());
        //查询手机号是否存在
        User user=userMapper.selectOne(wrapper);
        if(user != null) {
             userRegisterVo.setCode("0");
             return  userRegisterVo;
        }
        User user1 = new User();
        user1.setPhone(userLoginDto.getPhone());
        String PassWord = md5.md5Encrypt(userLoginDto.getPassword());
        user1.setPassword(PassWord);
        userMapper.insert(user1);
        User user2=userMapper.selectOne(wrapper);
        //封装
        userRegisterVo.setUserId(user2.getUserId());
        userRegisterVo.setPhone(user2.getPhone());
        userRegisterVo.setCode("1");
        return  userRegisterVo;
    }
}