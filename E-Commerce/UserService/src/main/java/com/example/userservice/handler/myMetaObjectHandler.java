package com.example.userservice.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
/**
 * @Author: LH
 * @Date: 2025/3/15 18:44
 */
//Mybatis自动填充
@Component
public class myMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
      this.strictInsertFill(metaObject,"createTime",Date.class, new Date());
      this.strictInsertFill(metaObject,"updateTime",Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject,"updateTime",Date.class,new Date());
    }
}