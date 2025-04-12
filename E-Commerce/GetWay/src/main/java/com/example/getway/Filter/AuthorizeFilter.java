package com.example.getway.Filter;
import com.example.getway.config.AuthProperties;
import com.example.getway.utils.jwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;

/**
 * @Author: LH
 * @Date: 2025/3/16 17:34
 */
@Component
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered {
    @Autowired
    AuthProperties authProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (isExclude(request.getPath().toString())) {
            return chain.filter(exchange);
        }
        String token = null;
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        List<HttpCookie> tokenCookie = cookies.get("token");
        if (tokenCookie != null && !tokenCookie.isEmpty()) {
            token = tokenCookie.get(0).getValue();  // 获取第一个 Cookie 的值
        }
        Object userId = new Object();
        try{
            Claims claims = jwtUtils.parseJWT(token);
            userId = claims.get("id");
        }catch (Exception ignored){
        }
        if(userId == null || token == null) {
            return chain.filter(exchange);
        }
        //传递用户值
        String userInfo = userId.toString();
        ServerWebExchange swe = exchange.mutate()
                .request(builder -> builder.header("user-info", userInfo))
                .build();
        return chain.filter(swe);
    }
    //判断路径是否要被拦截
    private boolean isExclude(String path) {
        for (String pathPattern : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(pathPattern, path)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public int getOrder() {
        return 0;
    }
}