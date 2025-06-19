package com.communication.platform.communicationplatform.servlet;

import com.communication.platform.communicationplatform.dao.UserDao;
import com.communication.platform.communicationplatform.entity.User;
import com.communication.platform.communicationplatform.util.MyBatisUtil;
import com.communication.platform.communicationplatform.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // 参数验证
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "用户名和密码不能为空");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        // 验证用户名密码
        UserDao userDao = MyBatisUtil.getSqlSession().getMapper(UserDao.class);
        User user = userDao.login(username, SecurityUtil.md5(password));

        if (user == null) {
            req.setAttribute("error", "用户名或密码错误");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        // 登录成功，保存用户信息到session
        SecurityUtil.setLoginUser(req.getSession(), user);

        // 重定向到首页
        resp.sendRedirect(req.getContextPath() + "/");
    }
} 