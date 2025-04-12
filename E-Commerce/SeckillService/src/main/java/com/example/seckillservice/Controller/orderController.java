package com.example.seckillservice.Controller;
import com.example.seckillservice.Entity.Dto.orderDto;
import com.example.seckillservice.Limiter.RedisRateLimiter;
import com.example.seckillservice.Mapper.orderMapper;
import com.example.seckillservice.Reuslt.Result;
import com.example.seckillservice.Service.orderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
     @Autowired
     private RedisRateLimiter rateLimiter;
    @PostMapping("/order")
    public Result orderById(@RequestBody orderDto orderDto){
        Result result = new Result();
        if (!rateLimiter.acquireToken(orderDto.getUserId())){
            result.setCode("3");
            return result;
        }
        return orderService.orderById(orderDto);
   }
   //支付
   @PostMapping("/pay/{id}")
   public Result pay(@PathVariable("id") Long id){
      return  orderService.payById(id);
   }
}