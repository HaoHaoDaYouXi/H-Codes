package com.haohaodayouxi.shiro.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/**
 * ShiroSessionListener
 *
 * @author TONE
 * @date 2024/8/26
 */
@Slf4j
public class ShiroSessionListener implements SessionListener {

    @Override
    public void onStart(Session session) {
        // 会话创建时触发
        log.info("ShiroSessionListener session {} 被创建", session.getId());
    }

    @Override
    public void onStop(Session session) {
        // 会话被停止时触发
        log.info("ShiroSessionListener session {} 被销毁", session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        //会话过期时触发
        log.info("ShiroSessionListener session {} 过期", session.getId());
    }
}
