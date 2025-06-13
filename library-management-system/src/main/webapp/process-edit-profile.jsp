<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.library.librarymanagementsystem.dao.UserDAO" %>
<%@ page import="com.library.librarymanagementsystem.entity.User" %>
<%@ page import="java.sql.SQLException" %>
<%
    request.setCharacterEncoding("UTF-8");
    
    // 检查用户是否已登录
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    User currentUser = (User) session.getAttribute("user");
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");
    
    // 验证用户名是否为空
    if (username == null || username.trim().isEmpty()) {
        request.setAttribute("errorMessage", "用户名不能为空。");
        request.getRequestDispatcher("edit-profile.jsp").forward(request, response);
        return;
    }
    
    // 如果输入了新密码，验证两次输入的密码是否一致
    if (password != null && !password.isEmpty()) {
        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "两次输入的密码不一致。");
            request.getRequestDispatcher("edit-profile.jsp").forward(request, response);
            return;
        }
    }
    
    UserDAO userDAO = new UserDAO();
    try {
        // 检查新用户名是否已被其他用户使用
        if (!username.equals(currentUser.getUsername()) && userDAO.existsByUsername(username)) {
            request.setAttribute("errorMessage", "该用户名已被使用，请选择其他用户名。");
            request.getRequestDispatcher("edit-profile.jsp").forward(request, response);
            return;
        }
        
        // 更新用户信息
        currentUser.setUsername(username);
        if (password != null && !password.isEmpty()) {
            currentUser.setPassword(password);
        }
        
        userDAO.updateUser(currentUser);
        
        // 更新session中的用户信息
        session.setAttribute("user", currentUser);
        
        // 设置成功消息
        request.setAttribute("successMessage", "个人信息修改成功！");
        request.getRequestDispatcher("edit-profile.jsp").forward(request, response);
    } catch (SQLException e) {
        e.printStackTrace();
        request.setAttribute("errorMessage", "数据库操作失败，请稍后再试。");
        request.getRequestDispatcher("edit-profile.jsp").forward(request, response);
    }
%> 