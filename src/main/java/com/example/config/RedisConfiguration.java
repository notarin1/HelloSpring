package com.example.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * Redisを用いたキャッシュ
 * 
 * @author notar_000
 *
 * @see http://qiita.com/yoshidan/items/f7c10a43d2a40c3ce8df
 * 
 */
@Slf4j
@Configuration
@EnableCaching
public class RedisConfiguration extends CachingConfigurerSupport {
    @Bean
    @Autowired
    public CacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
	RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);

	// キャッシュ有効期限の設定(秒)
	Map<String, Long> expires = new HashMap<String, Long>();
	expires.put("cache.day", new Long(24 * 60 * 60));
	expires.put("cache.short", new Long(3 * 60));
	cacheManager.setExpires(expires);
	return cacheManager;
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
	return new CacheErrorHandler() {
	    @Override
	    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
		log.error(String.format("%s:%s:%s", cache.getName(), key, exception.getMessage()), exception);
	    }

	    @Override
	    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
		log.error(String.format("%s:%s:%s", cache.getName(), key, exception.getMessage()), exception);
	    }

	    @Override
	    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
		log.error(String.format("%s:%s:%s", cache.getName(), key, exception.getMessage()), exception);
		log.error(exception.getMessage(), exception);
	    }

	    @Override
	    public void handleCacheClearError(RuntimeException exception, Cache cache) {
		log.error(String.format("%s:%s", cache.getName(), exception.getMessage()), exception);
		log.error(exception.getMessage(), exception);
	    }
	};
    }
}
