package com.example.seckillservice.Entity.Dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: LH
 * @Date: 2025/3/18 15:43
 */
@Data
public class orderDto implements Serializable {
    private  Long userId;
    private  Long productId;
}