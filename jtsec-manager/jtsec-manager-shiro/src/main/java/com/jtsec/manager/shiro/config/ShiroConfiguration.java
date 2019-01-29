package com.jtsec.manager.shiro.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;

/**
 * @author NovLi
 * @Description: TODO
 * @date 2018/7/10 10:35
 */
@Data
@Slf4j
@Configuration
public class ShiroConfiguration {

    /**
     * @param manager 注入SecurityManager 设置访问登录跳转/toLogin  登录成功跳转页/index  无权限跳转页/unAuthor
     * @Description: spring 声明shiroFilter
     * @Description filterChainDefinitionMap 拦截器链
     * @author NovLi
     * @date 2018/7/10 14:22
     */
    @Bean (name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier ("securityManager") SecurityManager manager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(manager);
        log.info("初始化 fitter！！！！！！！！！！！！！！");
        bean.setLoginUrl("/unLogin");
        bean.setUnauthorizedUrl("/unAuthor");

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<> ();
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/index", "authc");

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    /**
     * @param realm 注入Realm
     * @Description: Spring中声明securityManager
     * @author NovLi
     * @date 2018/7/10 14:08
     */
    @Bean("securityManager")
    public SecurityManager securityManager(@Qualifier ("customRealm") CustomRealm realm,
                                           @Qualifier("SessionManager") DefaultWebSessionManager sessionManager,
                                           @Qualifier("RedisCacheManager") RedisCacheManager cacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager ();
        securityManager.setRealm(realm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(cacheManager);
        return securityManager;
    }

    /**
     * @param matcher 注入密码校验 CacheManager 注入缓存机制
     * @Description: spring中声名自定义realm
     * @author NovLi
     * @date 2018/7/10 14:01
     */
    @Bean("customRealm")
    public CustomRealm customRealm(@Qualifier("customCredentialMatcher") CustomCredentialMatcher matcher) {
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(matcher);
        return customRealm;
    }

    /**
     * @Description: spring中声名自定义密码校验
     * @author NovLi
     * @date 2018/7/10 14:01
     */
    @Bean("customCredentialMatcher")
    public CustomCredentialMatcher customCredentialMatcher() {
        return new CustomCredentialMatcher();
    }

    /**
     * 　* @Description: redis 实现cache接口
     * 　* @author NovLi
     * 　* @date 2018/7/16 22:42
     */
    @Bean("RedisCacheManager")
    public RedisCacheManager redisCacheManager() {
        return new RedisCacheManager();
    }


    /**
     * @param RedisSessionDao 注入soul
     * @Description: 自定义sessionManager
     * @author NovLi
     * @date 2018/7/16 0:59
     */
    @Bean("SessionManager")
    public DefaultWebSessionManager sessionManager(@Qualifier("RedisSessionDao") RedisSessionDao RedisSessionDao,
                                                   @Qualifier("SimpleCookie") SimpleCookie simpleCookie) {
        CustomSessionManager sessionManager = new CustomSessionManager();
        sessionManager.setSessionIdCookie(simpleCookie);                                   //创建会话 Cookie 的模板
        sessionManager.setSessionDAO(RedisSessionDao);
        sessionManager.setSessionIdCookieEnabled(true);                                   //是否启用自定cookie名称
        return sessionManager;
    }

    /**
     * @Description: 定义RedisSessionDao
     * @author NovLi
     * @date 2018/7/16 0:58
     */
    @Bean("RedisSessionDao")
    public RedisSessionDao redisSessionDao() {
        return new RedisSessionDao();
    }

    /**
     * 　* @Description: 自定义rememberMe
     * 　* @author NovLi
     * 　* @date 2018/7/16 23:03
     */
    @Bean("RememberMeManager")
    public RememberMeManager rememberMeManager(@Qualifier("SimpleCookie") SimpleCookie simpleCookie) {
        RememberMeManager rememberMeManager = new CookieRememberMeManager ();
        ((CookieRememberMeManager) rememberMeManager).setCookie(simpleCookie);
        return rememberMeManager;
    }

    /**
     * 　* @Description: cookie
     * 　* @author NovLi
     * 　* @date 2018/7/16 23:03
     */
    @Bean("SimpleCookie")
    public SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("NovLi");             //cookie 名称
        simpleCookie.setMaxAge(-1);                //：设置 Cookie 的过期时间，秒为单位，默认-1 表示关闭浏览器时过期 Cookie
        simpleCookie.setHttpOnly(true);
        return simpleCookie;
    }

    @Bean("SessionListener")
    public SessionListener sessionListener() {
        return new CostomSessionListener();
    }

    /**
     * @param securityManager 注入
     * @return
     * @throws
     * @Description: spring 支持 shiro 及权限注解 可以在控制器中使用注解
     * @author NovLi
     * @date 2018/7/10 14:49
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * @param
     * @return
     * @throws
     * @Description: shiro代理servlet 在servlet接受到请求之前接受请求
     * @author NovLi
     * @date 2018/7/10 14:27
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

}
