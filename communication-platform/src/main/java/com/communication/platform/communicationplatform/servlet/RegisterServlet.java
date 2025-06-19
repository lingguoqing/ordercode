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
import java.time.LocalDateTime;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        // 参数验证
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty()) {
            req.setAttribute("error", "用户名和密码不能为空");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // 检查两次密码是否一致
        if (!password.equals(confirmPassword)) {
            req.setAttribute("error", "两次输入的密码不一致");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // 检查用户名长度
        if (username.length() < 3 || username.length() > 20) {
            req.setAttribute("error", "用户名长度必须在3-20个字符之间");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // 检查密码长度
        if (password.length() < 6 || password.length() > 20) {
            req.setAttribute("error", "密码长度必须在6-20个字符之间");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // 检查用户名是否已存在
        UserDao userDao = MyBatisUtil.getSqlSession().getMapper(UserDao.class);
        if (userDao.findByUsername(username) != null) {
            req.setAttribute("error", "用户名已存在");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // 创建新用户
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(SecurityUtil.md5(password));
        user.setCreateTime(LocalDateTime.now());

        // 保存用户
        userDao.insert(user);

        // 注册成功后自动登录
        SecurityUtil.setLoginUser(req.getSession(), user);

        // 重定向到首页
        resp.sendRedirect(req.getContextPath() + "/");
    }
} 