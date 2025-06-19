package com.communication.platform.communicationplatform.filter;

import com.communication.platform.communicationplatform.util.SecurityUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 设置字符编码
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        // 获取请求路径
        String path = req.getRequestURI().substring(req.getContextPath().length());
        String method = req.getMethod();

        // 允许直接访问的路径
        if (path.equals("/") || path.equals("/index.jsp") || 
            path.equals("/login") || path.equals("/register") || 
            path.startsWith("/static/") || path.endsWith(".css") || path.endsWith(".js") ||
            (path.equals("/question") && method.equals("GET"))) {
            chain.doFilter(request, response);
            return;
        }

        // 检查是否已登录
        if (SecurityUtil.getLoginUser(req.getSession()) == null) {
            // 如果是AJAX请求，返回JSON
            String xhr = req.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(xhr)) {
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().write("{\"code\": 401, \"message\": \"请先登录\"}");
                return;
            }
            // 普通请求重定向到登录页
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
} 