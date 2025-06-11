<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.library.librarymanagementsystem.dao.UserDAO" %>
<%@ page import="com.library.librarymanagementsystem.entity.User" %>
<%@ page import="java.sql.SQLException" %>
<%
    request.setCharacterEncoding("UTF-8");
    
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    
    UserDAO userDAO = new UserDAO();
    try {
        User user = userDAO.findByUsername(username, password);
        
        if (user != null) {
            // 登录成功，将用户信息存储在session中
            session.setAttribute("user", user);
            response.sendRedirect("book-list.jsp");
        } else {
            // 登录失败，设置错误消息并重定向到登录页面
            request.setAttribute("errorMessage", "用户名或密码错误。");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // 数据库操作失败，设置错误消息并重定向到登录页面
        request.setAttribute("errorMessage", "数据库操作失败，请稍后再试。");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
%> 