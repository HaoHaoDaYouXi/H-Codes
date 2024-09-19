package com.haohaodayouxi.cloud.utils;

import com.haohaodayouxi.common.util.business.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * I18nMessageUtils
 *
 * @author TONE
 * @date 2024/9/9
 */
@Slf4j
public class I18nMessageUtils {

    private static final MessageSource MESSAGE_SOURCE = SpringContextHolder.getBean(MessageSource.class);

    /**
     * 根据消息键和参数 获取消息
     *
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args) {
        try {
            return MESSAGE_SOURCE.getMessage(code, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            log.error("获取国际化消息失败，code:{}, args:{}", code, args, e);
            return code;
        }
    }
}
