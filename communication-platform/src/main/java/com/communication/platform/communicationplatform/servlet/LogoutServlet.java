package com.communication.platform.communicationplatform.servlet;

import com.communication.platform.communicationplatform.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 清除登录信息
        SecurityUtil.removeLoginUser(req.getSession());
        
        // 重定向到登录页
        resp.sendRedirect(req.getContextPath() + "/login");
    }
} 