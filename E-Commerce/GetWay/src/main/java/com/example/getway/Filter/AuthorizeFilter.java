package com.example.getway.Filter;
import com.example.getway.config.AuthProperties;
import com.example.getway.utils.jwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;
/**
 * @Author: LH
 * @Date: 2025/3/16 17:34
 */
@Component
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
        List<String> headers = request.getHeaders().get("authorization");
        Object userId = new Object();
        if (!CollectionUtils.isEmpty(headers)) {
            token = headers.get(0);
        }
        try{
            Claims claims = jwtUtils.parseJWT(token);
            userId = claims.get("id");
        }catch (Exception e){
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