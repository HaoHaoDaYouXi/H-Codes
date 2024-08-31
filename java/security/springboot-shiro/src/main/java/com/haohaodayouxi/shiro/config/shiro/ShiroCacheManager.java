package com.haohaodayouxi.shiro.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ShiroCacheManager
 *
 * @author TONE
 * @date 2024/8/27
 */
@Slf4j
public class ShiroCacheManager implements CacheManager {

    // fast lookup by name map
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        log.debug("获取名称为: {} 的RedisCache实例", name);

        Cache c = caches.get(name);

        if (c == null) {

            // create a new cache instance
            c = new ShiroCache();

            // add it to the cache collection
            caches.put(name, c);
        }
        return c;
    }

}
