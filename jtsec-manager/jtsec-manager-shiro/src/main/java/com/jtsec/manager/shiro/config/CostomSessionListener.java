package com.jtsec.manager.shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

@Slf4j
public class CostomSessionListener implements SessionListener {
    @Override
    public void onStart(Session session) {
        log.debug("会话创建：" + session.getId());
    }

    @Override
    public void onStop(Session session) {
        log.debug("会话停止：" + session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        log.debug("会话过期：" + session.getId());
    }
}
