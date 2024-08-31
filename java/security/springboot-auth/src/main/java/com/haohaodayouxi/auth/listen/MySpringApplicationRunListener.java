package com.haohaodayouxi.auth.listen;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.Duration;

/**
 * SpringBoot生命周期各个环节的监听器
 *
 * @author TONE
 * @date 2024/8/24
 */
public class MySpringApplicationRunListener implements SpringApplicationRunListener {
    /**
     * 这里的SpringApplication对象是事件源对象，所有的事件都是在这上面产生
     */
    public MySpringApplicationRunListener(SpringApplication application, String[] args) {
    }

    /**
     * 在 run 方法首次启动时立即调用。可用于非常早期的初始化。
     *
     * @param bootstrapContext the bootstrap context
     */
    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        // 基本用不到，可能会用于检测硬件条件
        System.out.println("Starting……启动中");
        SpringApplicationRunListener.super.starting(bootstrapContext);
    }

    /**
     * 在准备好环境后但在创建环境之前 ApplicationContext 调用。
     *
     * @param bootstrapContext the bootstrap context
     * @param environment      the environment
     */
    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        // 加载配置信息
        System.out.println("environmentPrepared……环境变量准备中");
        SpringApplicationRunListener.super.environmentPrepared(bootstrapContext, environment);
    }

    /**
     * 在创建并准备 之后 ApplicationContext ，但在加载源之前调用。
     *
     * @param context the application context
     */
    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("contextPrepared……上下文对象准备");
        SpringApplicationRunListener.super.contextPrepared(context);
    }

    /**
     * 在加载应用程序上下文后但在刷新应用程序上下文之前调用。
     *
     * @param context the application context
     */
    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("contextLoaded……上下文对象开始加载");
        SpringApplicationRunListener.super.contextLoaded(context);
    }


    /**
     * 上下文已刷新，应用程序已启动，但CommandLineRunnersApplicationRunners尚未被调用。
     *
     * @param context the application context.
     */
    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        // 到这个地方启动就完成了，Ioc容器就初始化好了
        System.out.println("started……上下文对象加载完成");
        SpringApplicationRunListener.super.started(context, timeTaken);
    }

    /**
     * 在 run 方法完成之前立即调用，此时应用程序上下文已刷新并且 all CommandLineRunners 和 ApplicationRunners 已被调用
     *
     * @param context   the application context.
     * @param timeTaken the time taken for the application to be ready or {@code null} if
     *                  unknown
     */
    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        System.out.println("ready……准备启动项目");
        SpringApplicationRunListener.super.ready(context, timeTaken);
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("failed……启动失败");
        SpringApplicationRunListener.super.failed(context, exception);
    }
}
