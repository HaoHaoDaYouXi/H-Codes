package com.haohaodayouxi.cloud.feign_service.factory;

import com.haohaodayouxi.cloud.feign_service.service.DemoFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 服务降级处理(熔断)，可以不写熔断
 * 熔断是多服务业务处理中断场景使用，如全部重置或补偿处理等业务情况
 *
 * @author lenovo
 */
@Slf4j
@Component
public class DemoFallbackFactory implements FallbackFactory<DemoFeignService> {

    @Override
    public DemoFeignService create(Throwable throwable) {
        log.error("服务调用失败:{}", throwable.getMessage());
        return new DemoFeignService() {
            @Override
            public String check() {
                return "暂时无法访问，请稍后尝试";
            }
        };
    }
}
