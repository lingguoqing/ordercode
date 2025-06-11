<%@ page import="com.library.librarymanagementsystem.entity.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // 检查用户是否已登录
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>图书管理系统 - 添加图书</title>
    <link href="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>添加新书</h2>
            <div>
                <span class="me-3">欢迎，<%= session.getAttribute("user") != null ? ((User)session.getAttribute("user")).getUsername() : "" %></span>
                <a href="logout.jsp" class="btn btn-outline-danger">退出登录</a>
            </div>
        </div>
        
        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null && !errorMessage.isEmpty()) {
        %>
                <div class="alert alert-danger" role="alert">
                    <%= errorMessage %>
                </div>
        <%
            }
        %>
        <form action="process-add-book.jsp" method="post">
            <div class="mb-3">
                <label for="title" class="form-label">书名</label>
                <input type="text" class="form-control" id="title" name="title" required>
            </div>
            <div class="mb-3">
                <label for="author" class="form-label">作者</label>
                <input type="text" class="form-control" id="author" name="author" required>
            </div>
            <div class="mb-3">
                <label for="publisher" class="form-label">出版社</label>
                <input type="text" class="form-control" id="publisher" name="publisher" required>
            </div>
            <div class="mb-3">
                <label for="publicationDate" class="form-label">出版日期</label>
                <input type="date" class="form-control" id="publicationDate" name="publicationDate" required>
            </div>
            <div class="mb-3">
                <label for="isbn" class="form-label">ISBN</label>
                <input type="text" class="form-control" id="isbn" name="isbn" required>
            </div>
            <div class="mb-3">
                <label for="quantity" class="form-label">库存数量</label>
                <input type="number" class="form-control" id="quantity" name="quantity" required min="0">
            </div>
            <button type="submit" class="btn btn-primary">添加</button>
            <a href="book-list.jsp" class="btn btn-secondary">返回</a>
        </form>
    </div>
    
    <script src="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/5.1.3/js/bootstrap.bundle.min.js"></script>
</body>
</html> 