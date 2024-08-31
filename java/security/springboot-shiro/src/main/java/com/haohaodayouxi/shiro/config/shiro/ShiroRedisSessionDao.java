package com.haohaodayouxi.shiro.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ShiroRedisSessionDao
 *
 * @author TONE
 * @date 2024/8/26
 */
@Slf4j
public class ShiroRedisSessionDao extends AbstractSessionDAO {

    @Value("${session.redis.expireTime}")
    private long expireTime;

    private final String SESSION_PREFIX = "shiro-session:";

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        String key = SESSION_PREFIX + session.getId();
        redisTemplate.opsForValue().set(key, session, expireTime, TimeUnit.SECONDS);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = null;
        if (ObjectUtils.isNotEmpty(sessionId)) {
            String key = SESSION_PREFIX + sessionId;
            Object o = redisTemplate.opsForValue().get(key);
            if (ObjectUtils.isNotEmpty(o)) {
                session = (Session) o;
            }
        }
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session != null && session.getId() != null) {
            session.setTimeout(expireTime * 1000);
            String key = SESSION_PREFIX + session.getId();
            redisTemplate.opsForValue().set(key, session, expireTime, TimeUnit.SECONDS);
        }
    }

    @Override
    public void delete(Session session) {
        if (session != null && session.getId() != null) {
            String key = SESSION_PREFIX + session.getId();
            redisTemplate.opsForValue().getOperations().delete(key);
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Object> keys = redisTemplate.keys(SESSION_PREFIX + "*");
        if (keys != null) {
            return keys.stream().map(o -> (Session) redisTemplate.opsForValue().get(o)).collect(Collectors.toSet());
        } else {
            return null;
        }
    }

}
