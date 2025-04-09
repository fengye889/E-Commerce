package com.example.seckillservice.Entity.Dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: LH
 * @Date: 2025/3/17 15:29
 */
@Data
public class ProductDto {
    private  Long id;
    private String name;

    private String description;

    private BigDecimal originalPrice;

    private BigDecimal seckillPrice;

    private Integer totalStock;

    private Integer seckillStock;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;
}