<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.library.librarymanagementsystem.entity.User" %>
<%
    // 检查用户是否已登录
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    User currentUser = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>图书管理系统 - 确认退出</title>
    <link href="assets/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="text-center">确认退出</h3>
                    </div>
                    <div class="card-body text-center">
                        <p class="mb-4">您好，<%= currentUser.getUsername() %></p>
                        <p class="mb-4">您确定要退出登录吗？</p>
                        <div class="d-flex justify-content-center gap-3">
                            <a href="logout.jsp" class="btn btn-danger">确认退出</a>
                            <a href="book-list.jsp" class="btn btn-secondary">取消</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="assets/bootstrap.bundle.min.js"></script>
</body>
</html> 