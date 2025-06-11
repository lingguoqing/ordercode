<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.library.librarymanagementsystem.dao.UserDAO" %>
<%@ page import="com.library.librarymanagementsystem.entity.User" %>
<%@ page import="java.sql.SQLException" %>
<%
    request.setCharacterEncoding("UTF-8");
    
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");
    
    // 验证两次输入的密码是否一致
    if (!password.equals(confirmPassword)) {
        request.setAttribute("errorMessage", "两次输入的密码不一致。");
        request.getRequestDispatcher("register.jsp").forward(request, response);
        return;
    }
    
    UserDAO userDAO = new UserDAO();
    try {
        // 检查用户名是否已存在
        if (userDAO.existsByUsername(username)) {
            request.setAttribute("errorMessage", "账户已存在，请尝试其他用户名。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // 创建新用户
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        
        userDAO.register(newUser);
        
        // 注册成功，重定向到登录页面
        response.sendRedirect("login.jsp?registered=1");
    } catch (SQLException e) {
        e.printStackTrace();
        request.setAttribute("errorMessage", "数据库操作失败，请稍后再试。");
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
%> 