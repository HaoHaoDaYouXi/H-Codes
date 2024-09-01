package com.haohaodayouxi.freemarker.config.freemarker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * @author TONE
 * @date 2024/9/01
 */
@Configuration
public class StaticResourceUrl {
    @Bean
    public CommandLineRunner customFreemarker(FreeMarkerViewResolver resolver) {
        return strings -> {
            // 增加视图
            resolver.setViewClass(MyFreemarkerView.class);
            // 添加自定义解析器
            /*Map map = resolver.getAttributesMap();
            map.put("conver", new MyConver());*/
        };
    }
}
