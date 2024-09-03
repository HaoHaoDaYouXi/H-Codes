package com.haohaodayouxi.auth2.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 事件监听器：负责处理发送的事件
 *
 * @author TONE
 * @date 2024/8/24
 */
@Slf4j
@Component
public class DemoEventListener {

    /**
     * 开启监听 DemoEvent 事件
     * 另一种写法：实现ApplicationListener<>的onApplicationEvent接口
     * <p>
     * 若想异步处理可以添加异步注解@Async
     *
     * @param demoEvent 事件
     */
    @EventListener
    public void downLoadCallBackListener(DemoEvent demoEvent) {
        log.info("=====消息回调方法=====");
        log.info("消息内容：{}", demoEvent.getStr());
    }
}
