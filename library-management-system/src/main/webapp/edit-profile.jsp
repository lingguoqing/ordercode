<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.library.librarymanagementsystem.entity.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // 检查用户是否已登录
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    User currentUser = (User) session.getAttribute("user");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>图书管理系统 - 修改个人信息</title>
    <link href="assets/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>修改个人信息</h2>
            <div>
                <span class="me-3">欢迎，<%= currentUser.getUsername() %></span>
                <a href="confirm-logout.jsp" class="btn btn-outline-danger">退出登录</a>
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
            
            String successMessage = (String) request.getAttribute("successMessage");
            if (successMessage != null && !successMessage.isEmpty()) {
        %>
                <div class="alert alert-success" role="alert">
                    <%= successMessage %>
                </div>
        <%
            }
        %>
        
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-body">
                        <form action="process-edit-profile.jsp" method="post">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="username" class="form-label">用户名</label>
                                    <input type="text" class="form-control" id="username" name="username" 
                                           value="<%= currentUser.getUsername() %>" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="email" class="form-label">电子邮箱</label>
                                    <input type="email" class="form-control" id="email" name="email" 
                                           value="<%= currentUser.getEmail() != null ? currentUser.getEmail() : "" %>">
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="password" class="form-label">新密码</label>
                                    <input type="password" class="form-control" id="password" name="password" 
                                           placeholder="如果不修改密码请留空">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="confirmPassword" class="form-label">确认新密码</label>
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" 
                                           placeholder="如果不修改密码请留空">
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="realName" class="form-label">真实姓名</label>
                                    <input type="text" class="form-control" id="realName" name="realName" 
                                           value="<%= currentUser.getRealName() != null ? currentUser.getRealName() : "" %>">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="phone" class="form-label">手机号码</label>
                                    <input type="tel" class="form-control" id="phone" name="phone" 
                                           value="<%= currentUser.getPhone() != null ? currentUser.getPhone() : "" %>">
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="gender" class="form-label">性别</label>
                                    <select class="form-select" id="gender" name="gender">
                                        <option value="">请选择</option>
                                        <option value="男" <%= "男".equals(currentUser.getGender()) ? "selected" : "" %>>男</option>
                                        <option value="女" <%= "女".equals(currentUser.getGender()) ? "selected" : "" %>>女</option>
                                        <option value="其他" <%= "其他".equals(currentUser.getGender()) ? "selected" : "" %>>其他</option>
                                    </select>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="birthDate" class="form-label">出生日期</label>
                                    <input type="date" class="form-control" id="birthDate" name="birthDate" 
                                           value="<%= currentUser.getBirthDate() != null ? dateFormat.format(currentUser.getBirthDate()) : "" %>">
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="address" class="form-label">地址</label>
                                <textarea class="form-control" id="address" name="address" rows="3"><%= currentUser.getAddress() != null ? currentUser.getAddress() : "" %></textarea>
                            </div>
                            
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary">保存修改</button>
                                <a href="book-list.jsp" class="btn btn-secondary">返回图书列表</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="assets/bootstrap.bundle.min.js"></script>
</body>
</html> 