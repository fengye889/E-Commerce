package com.example.productservice.Lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLock {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 加锁
    public boolean tryLock(String lockKey, String value, long timeout) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, value, timeout, TimeUnit.MILLISECONDS);
        return result != null && result; // true表示成功获得锁
    }

    // 释放锁
    public void unlock(String lockKey, String value) {
        // 使用 Lua 脚本保证释放锁时的原子性
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        redisTemplate.execute((RedisCallback<Object>) (connection) -> connection.eval(script.getBytes(), ReturnType.INTEGER, 1, lockKey.getBytes(), value.getBytes()));
    }
}
