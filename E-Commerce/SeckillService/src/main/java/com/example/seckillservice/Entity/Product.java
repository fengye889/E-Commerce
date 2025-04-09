package com.example.seckillservice.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: LH
 * @Date: 2025/3/17 15:01
 */
@TableName("product")
@Data
public class Product{

    @TableId(type = IdType.AUTO)  // 自增主键
    private Long id;

    @TableField("name")  // 映射字段
    private String name;

    @TableField("description")  // 映射字段
    private String description;

    @TableField("original_price")  // 映射字段
    private BigDecimal originalPrice;

    @TableField("seckill_price")  // 映射字段
    private BigDecimal seckillPrice;

    @TableField("total_stock")  // 映射字段
    private Long totalStock;

    @TableField("seckill_stock")  // 映射字段
    private Long seckillStock;

    @TableField("start_time")  // 映射字段
    private Date startTime;

    @TableField("end_time")  // 映射字段
    private Date endTime;

    @TableField("status")  // 映射字段
    private Integer status;

    @TableField(value = "create_time", fill = FieldFill.INSERT)  // 插入时自动填充
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)  // 插入或更新时自动填充
    private Date updateTime;

}
