package com.zero.tech.monitor.configuration.zookeeper;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class MonitorRedisConfig {
    @Bean
    public RedisTemplate<String, Object> configRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        //序列化器
        GenericFastJsonRedisSerializer valueSerializer = new GenericFastJsonRedisSerializer();
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        template.setValueSerializer(valueSerializer);
        template.setKeySerializer(keySerializer);
        return template;
    }
}
