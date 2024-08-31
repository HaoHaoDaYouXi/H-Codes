package com.netty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication(
        exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class}
)
public class AppBootstrap {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AppBootstrap.class, args);
        init(applicationContext);
    }

    private static void init(ApplicationContext applicationContext) {
        log.info("-----------启动成功，开始初始化-----------");
        try {
            new MyServer().run();
            // 模拟发送数据
//            new MyClient().run();
        } catch (Exception e) {
            log.error("-----------初始化异常-----------", e);
        }
    }

}
