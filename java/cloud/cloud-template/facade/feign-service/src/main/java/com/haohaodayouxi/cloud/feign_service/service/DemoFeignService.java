package com.haohaodayouxi.cloud.feign_service.service;

import com.haohaodayouxi.cloud.feign_service.factory.DemoFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * demo远程服务接口
 * name：指定FeignClient的名称，如果项目使用了Ribbon，name属性会作为微服务的名称，用于服务发现
 * contextId：接口类的ID，同一个name，不配置contextId会出现名称冲突问题，或通过spring.main.allow-bean-definition-overriding=true
 * fallbackFactory：容错的处理，可以知道熔断的异常信息。
 *
 * @author TONE
 * @date 2024/9/20
 */
@FeignClient(name = "cloud", contextId = "DemoFeignService", fallbackFactory = DemoFallbackFactory.class)
public interface DemoFeignService {

    /**
     * check
     *
     * @return Object
     */
    @GetMapping("/cloud/health/h-open/check")
    String check();

}
