package com.example.userservice.Entity.Vo;

import lombok.Data;

/**
 * @Author: LH
 * @Date: 2025/3/14 16:20
 */
@Data
public class userLoginVo {
    private  String code;
    private  String msg;
    private  int userId;
    private  String phone;
}