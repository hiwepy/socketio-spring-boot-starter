package com.corundumstudio.socketio.spring.boot;

import com.corundumstudio.socketio.store.RedisTemplateStoreFactory;
import com.corundumstudio.socketio.store.StoreFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.IOException;
import java.net.UnknownHostException;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@AutoConfigureBefore({ SocketioServerAutoConfiguration.class})
@ConditionalOnProperty(prefix = SocketioRedisTemplateProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ SocketioRedisTemplateProperties.class })
public class SocketioRedisTemplateConfiguration {

	public RedisTemplate<Object, Object> socketIoRedisTemplate(RedisConnectionFactory connectionFactory) throws UnknownHostException {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);

		// 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setKeySerializer(RedisSerializer.string());
        // 值采用json序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        // 设置hash key 和value序列化模式
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

		return redisTemplate;
	}

	public RedisMessageListenerContainer socketIoRedisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		// 序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
		container.setTopicSerializer(RedisSerializer.string());
		return container;
	}

	@Bean
	public StoreFactory clientStoreFactory(RedisConnectionFactory connectionFactory) throws IOException {
		return new RedisTemplateStoreFactory(socketIoRedisTemplate(connectionFactory), socketIoRedisMessageListenerContainer(connectionFactory));
	}

}
