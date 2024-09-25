package com.haohaodayouxi.mqtt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haohaodayouxi.mqtt.model.db.DemoId;

import java.util.List;

/**
 * DemoIdMapper
 *
 * @author TONE
 * @date 2024/8/24
 */
public interface DemoIdMapper extends BaseMapper<DemoId> {

    List<DemoId> getDemoIdList();
}
