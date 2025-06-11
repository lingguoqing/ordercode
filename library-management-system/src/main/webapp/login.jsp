<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>图书管理系统 - 登录</title>
    <link href="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="text-center">登录</h3>
                    </div>
                    <div class="card-body">
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
                        <form action="process-login.jsp" method="post">
                            <div class="mb-3">
                                <label for="username" class="form-label">用户名</label>
                                <input type="text" class="form-control" id="username" name="username" required>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">密码</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary">登录</button>
                            </div>
                        </form>
                        <div class="text-center mt-3">
                            <p>还没有账号？ <a href="register.jsp">立即注册</a></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/5.1.3/js/bootstrap.bundle.min.js"></script>
</body>
</html> 