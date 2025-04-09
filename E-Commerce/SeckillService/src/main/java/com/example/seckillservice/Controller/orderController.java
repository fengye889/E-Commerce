package com.example.seckillservice.Controller;
import com.example.seckillservice.Client.ProductClient;
import com.example.seckillservice.Entity.Dto.ProductDto;
import com.example.seckillservice.Entity.Dto.orderDto;
import com.example.seckillservice.Entity.Order;
import com.example.seckillservice.Entity.Product;
import com.example.seckillservice.Mapper.orderMapper;
import com.example.seckillservice.Reuslt.Result;
import com.example.seckillservice.Service.orderService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LH
 * @Date: 2025/3/19 11:13
 */
@RestController
@RequestMapping("/order")
public class orderController {
     @Autowired
     orderService orderService;
     @Autowired
    orderMapper orderMapper;
    @PostMapping("/order")
    public Result orderById(@RequestBody orderDto orderDto){
       return orderService.orderById(orderDto);
   }
   //支付
   @PostMapping("/pay/{id}")
   public Result pay(@PathVariable("id") Long id){
      return  orderService.payById(id);
   }
}