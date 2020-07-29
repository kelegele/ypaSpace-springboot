package com.kelegele.ypaSpace.config;

import com.kelegele.ypaSpace.config.interceptor.LoginInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    /**
     * CROS跨域的处理
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
                .allowCredentials(true).maxAge(3600);
    }

    /**
     * 这个方法才能在拦截器中自动注入查询数据库的对象
     */
    @Bean
    LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    /**
     * 配置生成器：添加一个拦截器，拦截路径为login以后的路径
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry ){
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**").excludePathPatterns("/file/**","/user/login", "/user/register", "/static");
    }

}
