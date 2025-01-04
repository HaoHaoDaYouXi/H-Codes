package com.haohaodayouxi.shiro.config.shiro;

import com.haohaodayouxi.common.redis.service.impl.CommonRedisServiceImpl;
import com.haohaodayouxi.common.util.constants.StringConstant;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * ShiroCache 重新实现 Cache
 *
 * @author TONE
 * @date 2024/8/27
 */
public class ShiroCache implements Cache<String, Object> {
    /**
     * The Redis key prefix for caches
     */
    private final String KEY_PREFIX = "shiro_redis_cache";

    @Resource
    private CommonRedisServiceImpl<Object> commonRedisService;

    private String getKey(String key) {
        return KEY_PREFIX + StringConstant.COLON + key;
    }

    @Override
    public Object get(String key) throws CacheException {
        return commonRedisService.get(getKey(key), Object.class);
    }

    @Override
    public Object put(String key, Object value) throws CacheException {
        commonRedisService.set(getKey(key), value);
        return value;
    }

    @Override
    public Object remove(String key) throws CacheException {
        Object o = commonRedisService.get(getKey(key), Object.class);
        if (ObjectUtils.isNotEmpty(o)) {
            commonRedisService.del(getKey(key));
        }
        return o;
    }

    @Override
    public void clear() throws CacheException {
        commonRedisService.delBySelectKeys(KEY_PREFIX + StringConstant.MATCHES_PATTERN);
    }

    @Override
    public int size() {
        Set<String> keys = commonRedisService.keys(KEY_PREFIX + StringConstant.MATCHES_PATTERN);
        return ObjectUtils.isEmpty(keys) ? 0 : keys().size();
    }

    @Override
    public Set<String> keys() {
        return commonRedisService.keys(KEY_PREFIX + StringConstant.MATCHES_PATTERN);
    }

    @Override
    public Collection<Object> values() {
        return commonRedisService.batchGetByKeys(new ArrayList<>(keys()), Object.class, false);
    }
}
