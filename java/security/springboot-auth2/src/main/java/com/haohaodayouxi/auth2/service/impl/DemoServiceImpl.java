package com.haohaodayouxi.auth2.service.impl;

import com.haohaodayouxi.auth2.event.DemoEvent;
import com.haohaodayouxi.auth2.service.DemoService;
import com.haohaodayouxi.common.core.interfaces.BaseInfo;
import com.haohaodayouxi.common.core.interfaces.InitService;
import com.haohaodayouxi.common.util.base.SubBatchUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DemoServiceImpl
 *
 * @author TONE
 * @date 2024/8/24
 */
@Slf4j
@Service
public class DemoServiceImpl implements BaseInfo, InitService, DemoService {

    /**
     * 发布程序内部消息使用，类似MQ的mqTemplate
     */
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String info() {
        log.info("示例业务层");
        return "示例业务层";
    }

    @Override
    public void init() {
        log.info("初始化执行示例");
        publishDemoEvent();
        // 需要初始化的方法...
    }

    /**
     * 发送消息示例
     */
    public void publishDemoEvent() {
        String str = "LocalDateTime：{}" + LocalDateTime.now();
        applicationEventPublisher.publishEvent(new DemoEvent(str));
        log.info("publishDemoEvent str：{}", str);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert() {
        List<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(7);
        list.add(8);
        list.add(9);
        SubBatchUtil.listSubAccept(list, (data) -> log.info("分割后需要处理的数据：{}", data));
    }
}
