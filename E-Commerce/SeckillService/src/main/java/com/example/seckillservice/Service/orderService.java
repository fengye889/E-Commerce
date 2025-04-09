package com.example.seckillservice.Service;

import com.example.seckillservice.Entity.Dto.orderDto;
import com.example.seckillservice.Reuslt.Result;

/**
 * @Author: LH
 * @Date: 2025/3/19 11:19
 */
public interface orderService {
    Result orderById(orderDto orderDto);

    Result payById(Long id);
}
