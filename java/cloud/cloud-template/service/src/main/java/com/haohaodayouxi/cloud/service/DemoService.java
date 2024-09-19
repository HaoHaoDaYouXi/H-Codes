package com.haohaodayouxi.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haohaodayouxi.cloud.model.db.DemoId;
import com.haohaodayouxi.common.core.model.req.page.PageBaseReq;
import com.haohaodayouxi.common.core.model.vo.page.PageBaseVO;

/**
 * DemoService
 *
 * @author TONE
 * @date 2024/8/24
 */
public interface DemoService extends IService<DemoId> {
    /**
     * 批量新增示例
     */
    void batchInsert();

    PageBaseVO<DemoId> getList(PageBaseReq req);
}
