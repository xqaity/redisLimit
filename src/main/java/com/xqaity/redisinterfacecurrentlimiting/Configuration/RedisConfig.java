package com.xqaity.redisinterfacecurrentlimiting.Configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author Created by lenovo
 * @date 2022/5/20 13:41
 */
@Configuration
public class RedisConfig {
	@Autowired
	private RedisConnectionFactory connectionFactory;

	@Bean
	public RedisTemplate<Object, Object> redisTemplate() {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		// 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		redisTemplate.setKeySerializer(jackson2JsonRedisSerializer);
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		return redisTemplate;
	}
//	@Bean
//	public DefaultRedisScript<Long> limitScript() {
//		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
//		redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/limit.lua")));
//		redisScript.setResultType(Long.class);
//		return redisScript;
//	}
	@Bean("lua1")
	public DefaultRedisScript<Long> redisLimitScript(){
		DefaultRedisScript redisScript = new DefaultRedisScript<Long>();
		redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/limit1.lua")));
		return redisScript;
	}

}
