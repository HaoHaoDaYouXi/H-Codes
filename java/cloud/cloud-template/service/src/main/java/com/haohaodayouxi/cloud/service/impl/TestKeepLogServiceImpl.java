package com.haohaodayouxi.cloud.service.impl;

import com.haohaodayouxi.common.log.model.bo.ExecuteLogBO;
import com.haohaodayouxi.common.log.service.KeepLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * TestKeepLogServiceImpl
 *
 * @author TONE
 * @date 2024/9/5
 */
@Slf4j
@Service("TestKeepLogServiceImpl")
public class TestKeepLogServiceImpl implements KeepLogService {
    @Override
    public void saveLog(ExecuteLogBO executeLogBO) {
        log.debug("TestKeepLogServiceImpl");
    }
}
