package com.example.seckillservice.Entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

/**
 * @Author: LH
 * @Date: 2025/3/18 15:16
 */
@Data
@TableName("seckill_order")
public class Order  {
    @TableId(type = IdType.AUTO,value = "order_id")
    private  Long orderId;
    @TableField("user_id")
    private  Long userId;
    @TableField("product_id")
    private  Long productId;
    private  int status;
    private  int version;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
}