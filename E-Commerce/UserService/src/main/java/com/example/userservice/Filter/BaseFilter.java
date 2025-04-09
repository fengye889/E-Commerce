package com.example.userservice.Filter;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;
/**
 * 请求拦截，避免服务绕过接口被直接访问
 * @author LH
 */
@Component
@WebFilter(filterName = "BaseFilter",urlPatterns = {"/user/**"})
@Slf4j
public class BaseFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String requestURI = request.getRequestURI();
        if("/user/login".equals(requestURI) || "/user/register".equals(requestURI)){
            filterChain.doFilter(servletRequest, servletResponse);
        }
        // 获取请求头中的 "user-info" 值
        String userInfo = request.getHeader("user-info");
        if(userInfo != null){
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}