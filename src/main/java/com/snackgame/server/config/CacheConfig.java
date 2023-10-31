package com.snackgame.server.config;

import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public Cache gamesCache() {
        return new CaffeineCache(
                "games",
                Caffeine.newBuilder().maximumSize(100).build()
        );
    }

    @Bean
    public CacheManager cacheManager(List<Cache> caches) {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
