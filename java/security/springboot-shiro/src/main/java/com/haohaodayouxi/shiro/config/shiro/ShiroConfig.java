package com.haohaodayouxi.shiro.config.shiro;

import com.haohaodayouxi.common.util.constants.AlgorithmName;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.*;

/**
 * ShiroConfig
 * Shiro使用：主要涉及到ShiroFilterFactoryBean和SecurityManager中的认证器（Realm）、会话管理（SessionManager）、缓存管理（CacheManager）这些的具体实现
 *
 * @author TONE
 * @date 2024/8/26
 */
@Slf4j
@Configuration
public class ShiroConfig {

    /**
     * FilterRegistrationBean
     *
     * @return
     * @see org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration 已经实现了，可以不用再次注册
     */
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
//        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
//        filterRegistration.setEnabled(true);
//        filterRegistration.addUrlPatterns("/*");
//        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
//        return filterRegistration;
//    }

    /**
     * @return
     * @see org.apache.shiro.spring.web.ShiroFilterFactoryBean
     */
    @Bean(name = {"shiroFilter", "shiroFilterFactoryBean"})
    @DependsOn(value = "shiroSecurityManager")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("shiroSecurityManager") DefaultWebSecurityManager shiroSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(shiroSecurityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面，设置后就会走接口
        bean.setLoginUrl("/login");
        // 未授权
        bean.setUnauthorizedUrl("/error/403");
        // 登录成功后的url
        bean.setSuccessUrl("/index.htm");
        // 定义拦截器 anon 不需要认证,perms 需要认证
        Map<String, Filter> filters = new HashMap<>();
        filters.put("anon", new AnonymousFilter());
        filters.put("perms", shiroRequestUrl());
//        filters.put("jwt", shiroJwtFilter());
        bean.setFilters(filters);

        LinkedHashMap<String, String> chains = new LinkedHashMap<>();
        // 所有登录相关的也不用认证
        chains.put("/login/**", "anon");
        // 开启shiro内置退出过滤器，完成退出功能（不用自己写退出方法）
        // chains.put("/logout", "logout");
        // 静态资源 不需要认证
        chains.put("/static-res/**", "anon");
        // 设置必须认证才能访问的 perms=shiroRequestUrl
        chains.put("/**", "perms");
        // 也可以替换成 jwt 模式
        // chains.put("/**", "jwt");
        // 设置shiro的内置过滤器，配置访问权限
        bean.setFilterChainDefinitionMap(chains);
        return bean;
    }

    @Bean
    public ShiroRequestUrl shiroRequestUrl() {
        return new ShiroRequestUrl();
    }
//    @Bean
//    public ShiroJwtFilter shiroJwtFilter() {
//        return new ShiroJwtFilter();
//    }

    /**
     * 安全管理器
     * // @DependsOn 是为了确保需要使用的先加载，也可以改为直接调用方法的写法，如：securityManager.setRealm(shiroRealm());
     *
     * @return DefaultWebSecurityManager
     */
    @Bean(value = "shiroSecurityManager")
    @DependsOn(value = {"shiroRealm", "webSessionManager", "shiroCacheManager"})
    public DefaultWebSecurityManager shiroSecurityManager(
            @Qualifier("shiroRealm") ShiroRealm shiroRealm
            , @Qualifier("webSessionManager") DefaultWebSessionManager sessionManager
            , @Qualifier("shiroCacheManager") ShiroCacheManager shiroCacheManager
    ) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
//        关闭shiro自带的session，详情见文档：https://shiro.apache.org/session-management.html#SessionManagement-SessionsSubjectState-StatelessApplications
//        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
//        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
//        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
//        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
//        securityManager.setSubjectDAO(subjectDAO);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(shiroCacheManager);
        return securityManager;
    }

    /**
     * 自定义Realm
     *
     * @return
     * @see ShiroRealm
     */
    @Bean(name = "shiroRealm")
    public ShiroRealm shiroRealm() {
        ShiroRealm userRealm = new ShiroRealm();
        userRealm.setCredentialsMatcher(shiroCredentialsMatcher());
        userRealm.setCacheManager(cacheManager());
        userRealm.setCachingEnabled(true);
        userRealm.setAuthenticationCachingEnabled(false);
        userRealm.setAuthorizationCachingEnabled(true);
        return userRealm;
    }

    /**
     * 自定义缓存管理器，也可以使用RedisCacheManager
     *
     * @return shiroCacheManager
     */
    @Bean(name = "shiroCacheManager")
    public ShiroCacheManager cacheManager() {
        return new ShiroCacheManager();
    }

    /**
     * 凭证匹配器（密码校验交给Shiro的SimpleAuthenticationInfo进行处理）
     *
     * @return HashedCredentialsMatcher
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        hashedCredentialsMatcher.setHashAlgorithmName(AlgorithmName.MD5);// 散列算法MD5
        hashedCredentialsMatcher.setHashIterations(2);// 散列的次数，散列两次，相当于 md5(md5(""));

        return hashedCredentialsMatcher;
    }

    /**
     * 自定义密码校验
     *
     * @return shiroCredentialsMatcher
     */
    @Bean(name = "shiroCredentialsMatcher")
    public ShiroCredentialsMatcher shiroCredentialsMatcher() {
        return new ShiroCredentialsMatcher();
    }

    /**
     * DefaultWebSessionManager
     *
     * @return
     */
    @Bean(name = "webSessionManager")
    public DefaultWebSessionManager webSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> collection = new ArrayList<>();
        collection.add(sessionListener());
        sessionManager.setSessionListeners(collection);
        sessionManager.setSessionDAO(shiroSessionDAO());
        sessionManager.setSessionIdCookie(simpleCookie());
        return sessionManager;
    }

    /**
     * 自定义sessionListener
     *
     * @return sessionListener
     */
    @Bean(name = {"SessionListener", "sessionListener"})
    public ShiroSessionListener sessionListener() {
        return new ShiroSessionListener();
    }


    /**
     * 自定义SessionDAO，也可以直接使用RedisSessionDAO
     *
     * @return shiroSessionDAO
     */
    @Bean(name = "shiroSessionDAO")
    public ShiroSessionDAO shiroSessionDAO() {
        return new ShiroSessionDAO();
    }

    /**
     * simpleCookie,不定义在集群环境下会出现There is no session with id ....
     */
    @Bean(name = "simpleCookie")
    public SimpleCookie simpleCookie() {
        //  cookie的name,对应的默认是 JSESSIONID
        SimpleCookie cookie = new SimpleCookie("JSESSIONID");
        // 浏览器 脚本 都能取到 cookie
        cookie.setHttpOnly(false);
        // path为 / 用于多个系统共享JSESSIONID
        cookie.setPath("/");
        return cookie;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 配置LifecycleBeanPostProcessor，可以来自动的调用配置在Spring IOC容器中 Shiro Bean 的生命周期方法
     *
     * @return LifecycleBeanPostProcessor
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 支持在SpringBoot的Controller使用@RequiresPermission()等标签注解
     */
    @Bean("authorizationAttributeSourceAdvisor")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("shiroSecurityManager") DefaultWebSecurityManager shiroSecurityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(shiroSecurityManager);
        return advisor;
    }
}
