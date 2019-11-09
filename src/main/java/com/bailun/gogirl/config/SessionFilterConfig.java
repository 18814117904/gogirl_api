package com.bailun.gogirl.config;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bailun.gogirl.filter.OriginFilter;
import com.bailun.gogirl.filter.SessionFilter;

@Configuration
public class SessionFilterConfig {
//    @Bean
//    MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setLocation("");
//        return factory.createMultipartConfig();
//    }
    @Bean
    public FilterRegistrationBean<Filter> someFilterRegistration1() {
        //新建过滤器注册类
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
        // 添加我们写好的过滤器
        registration.setFilter( new OriginFilter());
//        registration.setFilter( new SessionFilter());
        // 设置过滤器的URL模式
        registration.addUrlPatterns("/*");
        return registration;
    }

}