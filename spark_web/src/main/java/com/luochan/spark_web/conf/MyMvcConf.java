package com.luochan.spark_web.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConf implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer webMvcConfigurer()
    {
        WebMvcConfigurer conf=new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {

            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/index1.html").setViewName("pages/index1");

            }
        };
        return conf;
    }
}
