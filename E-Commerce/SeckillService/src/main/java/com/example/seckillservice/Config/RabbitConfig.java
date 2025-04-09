package com.example.seckillservice.Config;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // 定义库存扣减消息的队列
    @Bean
    public Queue seckillQueue() {
        return new Queue("seckillQueue", true); // 创建一个持久化队列
    }
    //定义回滚消息队列
    @Bean
    public Queue seckillRollbackQueue() {
        return new Queue("seckillRollbackQueue", true);  // 队列持久化
    }
    // 定义订单生成确认消息的队列
    @Bean
    public Queue seckillConfirmQueue() {
        return new Queue("seckillConfirmQueue", true);  // 队列持久化
    }
    // 定义交换机
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("seckillExchange");
    }
    // 定义队列与交换机之间的绑定关系
    @Bean
    public Binding binding(Queue seckillQueue, TopicExchange exchange) {
        return BindingBuilder.bind(seckillQueue).to(exchange).with("seckill.routing");
    }
    @Bean
    public Binding seckillConfirmBinding(Queue seckillConfirmQueue,TopicExchange exchange) {
        return BindingBuilder.bind(seckillConfirmQueue)
                .to(exchange)
                .with("seckill.confirm.routing");  // 路由键
    }
    @Bean
    public Binding seckillRollbackBinding(Queue seckillRollbackQueue,TopicExchange exchange) {
        return BindingBuilder.bind(seckillRollbackQueue)
                .to(exchange)
                .with("seckill.rollback.routing");  // 路由键
    }
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        jackson2JsonMessageConverter.setCreateMessageIds(true);
        return jackson2JsonMessageConverter;
    }

}
