package com.example.seckillservice.Service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckillservice.Client.ProductClient;
import com.example.seckillservice.Entity.Dto.ProductDto;
import com.example.seckillservice.Entity.Dto.orderDto;
import com.example.seckillservice.Entity.Order;
import com.example.seckillservice.Entity.Product;
import com.example.seckillservice.Lock.RedisLock;
import com.example.seckillservice.Mapper.orderMapper;
import com.example.seckillservice.Reuslt.Result;
import com.example.seckillservice.Service.orderService;
import com.example.seckillservice.Socket.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 * @Author: LH
 * @Date: 2025/3/19 11:19
 */
@Slf4j
@Service
public class orderServiceimpl implements orderService {
    @Autowired
    ProductClient productClient;
    @Autowired
    private WebSocketService webSocketService;
    private static final String ORDER_QUEUE = "orderQueue";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    orderMapper  orderMapper;
    @Autowired
    RedisLock redisLock;
    private static final String LOCK_KEY = "product_query_lock"; // 锁的key
    private static final long LOCK_TIMEOUT = 3000; // 锁的超时时间
    //定时进行库存预热
    @Scheduled(cron = "0 0/10 * * * ?")
    public  void initStock(){
        List<Product> products = productClient.getAllProducts();
        for(Product product: products){
            String productKey ="stock_count:"+product.getId();
            redisTemplate.opsForValue().set(productKey,product.getSeckillStock(),30, TimeUnit.MINUTES);
        }
    }
    @Override
    public Result orderById(orderDto orderDto) {
        String lockValue = String.valueOf(System.nanoTime());
        Result result =new Result();
        //使用分布式锁来进行库存预减
        if(redisLock.tryLock(LOCK_KEY,lockValue,LOCK_TIMEOUT)){
            try {
                if(redisTemplate.hasKey(orderDto.getUserId()+":"+orderDto.getProductId())){
                    result.setCode("0");
                    return result;
                }
                String ProductKey="stock_count:"+orderDto.getProductId();
                //扣减库存,防止超卖
                Long stock = redisTemplate.opsForValue().decrement(ProductKey);
                if(stock == null || stock<0){
                    //还原库存
                    redisTemplate.opsForValue().increment(ProductKey);
                    result.setCode("2");
                    return  result;
                }
                redisTemplate.opsForValue().setIfAbsent(orderDto.getUserId()+":"+orderDto.getProductId(), "1",30,TimeUnit.MINUTES);
                //将订单放入消息队列
                rabbitTemplate.convertAndSend("seckillExchange", "seckill.routing", orderDto);
            } finally {
                redisLock.unlock(LOCK_KEY,lockValue);
            }
        }
       //检查用户是否已经下单
        result.setCode("1");
        return result;
    }
    @Override
    public Result payById(Long id){
        Order order = new Order();
        order.setOrderId(id);
        order.setStatus(1);
        //修改订单状态
        orderMapper.updateById(order);
        //删减商品库存
        Product product = productClient.getProductById(order.getProductId());
        product.setSeckillStock(product.getSeckillStock()-1);
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product,productDto);
        productClient.alterProduct(productDto);
        Result result =new Result();
        result.setCode("1");
        return  result;
    }
    //监听并且生成订单
    @RabbitListener(queues = "seckillQueue")
    public void processOrder(orderDto orderDto) {
        if (orderDto == null ) {
            // 处理空消息的逻辑
            return;
        }
        try{
           Order order =new Order();
           order.setUserId(orderDto.getUserId());
           order.setProductId(orderDto.getProductId());
           //生成订单
            orderMapper.insert(order);
        //生成的订单id返回给前端
            rabbitTemplate.convertAndSend("seckillExchange", "seckill.confirm.routing",order);
        }catch (Exception e){
            log.info(String.valueOf(e));
            //回滚
            rabbitTemplate.convertAndSend("seckillExchange", "seckill.rollback.routing", orderDto.getProductId());
        }
    }
    //订单生成失败回滚
    @RabbitListener(queues = "seckillRollbackQueue")
    public void rollbackStock(Long productId) {
        log.info("seckillRollback");
        // 回滚库存
        redisTemplate.opsForValue().increment("stock_count:" + productId);
    }
    //确认订单生成成功
    @RabbitListener(queues = "seckillConfirmQueue")
    public void confirmOrder(Order order) {
        try{
            Order order1 = orderMapper.selectById(order.getOrderId());
            if(order1 != null){
                //将订单消息发送给前端
                String orderMessage = String.valueOf(order.getOrderId());
                webSocketService.sendMessage(orderMessage);
                log.info("seckillConfirm!");
            }
        }catch (Exception e){
              return;
        }
    }
    //定时检查订单是否超时
    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkTimeoutOrders() {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",0);
        List<Order> orders = orderMapper.selectList(queryWrapper);
        //处理超时的订单
        for(Order order : orders){
            Date date =new Date();
            Instant instant = date.toInstant();
            Instant instant1 = order.getCreateTime().toInstant();
            Duration duration = Duration.between(instant,instant1);
            long seconds = Math.abs(duration.getSeconds());
            log.info(String.valueOf(seconds));
            String ProductKey="stock_count:"+order.getProductId();
            if(seconds >30*60){
                //回调库存
                redisTemplate.opsForValue().increment(ProductKey);
                //清除超时订单
                orderMapper.deleteById(order.getOrderId());
                log.info("order timeout"+order.getOrderId());
            }
        }
    }
    //定时处理库存保证数据的一致性
    @Scheduled(cron = "0 0/10 * * * ?")
    public void checkOrderStock(){
        List<Product> products = productClient.getAllProducts();
        for(Product product : products){
            String countKey="stock_count:"+product.getId();
           if(redisTemplate.opsForValue().get(countKey) != product.getSeckillStock()){
               redisTemplate.opsForValue().set(countKey,product.getSeckillStock(),30,TimeUnit.MINUTES);
           }
        }
    }
}