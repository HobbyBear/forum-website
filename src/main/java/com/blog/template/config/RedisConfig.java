package com.blog.template.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.*;


@Configuration
@Slf4j
public class RedisConfig {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Bean
    public HashOperations<String,String,String> hashOperations(){
       return redisTemplate.opsForHash();
    }

    @Bean
    public ZSetOperations<String,String> zSetOperations(){
        return redisTemplate.opsForZSet();
    }

    @Bean
    public SetOperations<String,String> setOperations(){
        return redisTemplate.opsForSet();
    }

    @Bean
    public ValueOperations<String,String> valueOperations(){
        return redisTemplate.opsForValue();
    }



}