package com.haohaodayouxi.shiro.config.shiro;

import com.haohaodayouxi.common.redis.service.impl.CommonRedisServiceImpl;
import com.haohaodayouxi.common.util.constants.StringConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ShiroSessionDAO
 *
 * @author TONE
 * @date 2024/8/27
 */
@Slf4j
public class ShiroSessionDAO extends AbstractSessionDAO {

    /**
     * The Redis key prefix for the sessions
     */
    private final String KEY_PREFIX = "shiro_redis_session";

    @Resource
    private CommonRedisServiceImpl<Session> commonRedisService;

    private String getKey(String key) {
        return KEY_PREFIX + StringConstant.COLON + key;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
    }

    /**
     * save session
     *
     * @param session
     * @throws UnknownSessionException
     */
    private void saveSession(Session session) throws UnknownSessionException {
        if (checkSession(session)) return;
        commonRedisService.set(getKey(session.getId().toString()), session);
        commonRedisService.expire(getKey(session.getId().toString()), 2L, TimeUnit.HOURS);
    }

    @Override
    public void delete(Session session) {
        if (checkSession(session)) return;
        commonRedisService.del(getKey(session.getId().toString()));
    }

    private static boolean checkSession(Session session) {
        if (session == null || session.getId() == null) {
            log.error("session or session id is null");
            return true;
        }
        return false;
    }

    @Override
    public Collection<Session> getActiveSessions() {
        List<Session> data = commonRedisService.batchGetByKeys(Collections.singletonList(KEY_PREFIX + StringConstant.MATCHES_PATTERN), Session.class);
        if (ObjectUtils.isNotEmpty(data)) {
            return new HashSet<>(data);
        }
        return new HashSet<>();
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            log.error("session id is null");
            return null;
        }
        return commonRedisService.get(getKey(sessionId.toString()), Session.class);
    }

}
