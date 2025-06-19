<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.communication.platform.communicationplatform.entity.User" %>
<%@ page import="com.communication.platform.communicationplatform.util.SecurityUtil" %>
<%
    User loginUser = SecurityUtil.getLoginUser(session);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${param.title} - 问答平台</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
        }
        .header {
            background-color: #4CAF50;
            padding: 15px 0;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        .nav {
            max-width: 1200px;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0 20px;
        }
        .nav a {
            color: white;
            text-decoration: none;
            padding: 8px 15px;
            border-radius: 4px;
        }
        .nav a:hover {
            background-color: rgba(255, 255, 255, 0.1);
        }
        .nav-left a {
            font-size: 20px;
            font-weight: bold;
        }
        .nav-right {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        .btn-ask {
            background-color: white;
            color: #4CAF50 !important;
            padding: 8px 20px !important;
            font-weight: bold;
        }
        .container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 0 20px;
        }
        .user-info {
            color: white;
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="nav">
            <div class="nav-left">
                <a href="${pageContext.request.contextPath}/">问答平台</a>
            </div>
            <div class="nav-right">
                <% if (loginUser != null) { %>
                    <a href="${pageContext.request.contextPath}/ask" class="btn-ask">提问</a>
                    <span class="user-info"><%=loginUser.getUsername()%></span>
                    <a href="${pageContext.request.contextPath}/logout">退出</a>
                <% } else { %>
                    <a href="${pageContext.request.contextPath}/login">登录</a>
                    <a href="${pageContext.request.contextPath}/register">注册</a>
                <% } %>
            </div>
        </div>
    </div>
    <div class="container"> 