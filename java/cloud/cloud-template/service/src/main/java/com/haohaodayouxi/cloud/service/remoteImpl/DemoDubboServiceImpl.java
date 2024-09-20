package com.haohaodayouxi.cloud.service.remoteImpl;

import com.alibaba.fastjson2.JSON;
import com.haohaodayouxi.cloud.dubbo_service.DemoDubboService;
import com.haohaodayouxi.cloud.model.db.DemoId;
import com.haohaodayouxi.cloud.service.DemoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * DemoDubboServiceImpl
 *
 * @author TONE
 * @date 2024/9/20
 */
@Slf4j
@DubboService
public class DemoDubboServiceImpl implements DemoDubboService {

    @Resource
    private DemoService demoService;

    @Override
    public String get() {
        DemoId demoId = demoService.getById(1);
        return JSON.toJSONString(demoId);
    }
}
