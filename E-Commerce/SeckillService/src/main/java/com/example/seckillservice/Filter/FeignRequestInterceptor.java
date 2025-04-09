package com.example.seckillservice.Filter;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
/**
 * @author LH
 * 防止Feign调用其他服务被拦截
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("user-info", "1"); // 设置统一的 userinfo 头部
    }
}
