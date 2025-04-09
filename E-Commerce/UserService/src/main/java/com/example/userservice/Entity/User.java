package com.example.userservice.Entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Value;

import java.util.Date;

/**
 * @Author: LH
 * @Date: 2025/3/13 17:34
 */
@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private  int userId;
    private  String phone;
    private String password;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}