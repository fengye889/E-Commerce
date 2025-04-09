package com.example.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.userservice.Entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: LH
 * @Date: 2025/3/14 15:38
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
