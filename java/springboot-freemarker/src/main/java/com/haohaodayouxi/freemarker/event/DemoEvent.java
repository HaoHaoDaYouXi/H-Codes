package com.haohaodayouxi.freemarker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 事件对象
 *
 * @author TONE
 * @date 2024/8/24
 */
@Getter
public class DemoEvent extends ApplicationEvent {
    private static final long serialVersionUID = 2265140463184177170L;
    /**
     * 事件内容 具体参数可以自行定义
     */
    private final String str;

    public DemoEvent(String str) {
        super(str);
        this.str = str;
    }
}
