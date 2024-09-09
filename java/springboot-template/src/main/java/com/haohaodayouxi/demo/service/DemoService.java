package com.haohaodayouxi.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haohaodayouxi.common.core.model.req.page.PageBaseReq;
import com.haohaodayouxi.common.core.model.vo.page.PageBaseVO;
import com.haohaodayouxi.demo.model.db.DemoId;

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
