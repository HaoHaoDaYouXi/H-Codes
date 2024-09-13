package com.haohaodayouxi.sharding_jdbc;

import com.haohaodayouxi.common.core.interfaces.InitService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * JDBCBootstrap
 *
 * @author TONE
 * @date 2024/8/31
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.haohaodayouxi"})
@MapperScan(basePackages = {"com.haohaodayouxi.sharding_jdbc.mapper"})
public class JDBCBootstrap {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(JDBCBootstrap.class, args);
        init(applicationContext);
    }

    private static void init(ApplicationContext applicationContext) {
        log.info("-----------启动成功，开始初始化-----------");
        try {
            // 调用初始化方法
            Map<String, InitService> beansOfType = applicationContext.getBeansOfType(InitService.class);
            beansOfType.forEach((name, value) -> value.init());
            log.info("-----------初始化成功-----------");
        } catch (Exception e) {
            log.error("-----------初始化异常-----------");
            // 自行判断是否要结束项目
            // System.exit(1);
        }
    }
}
