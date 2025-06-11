<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // 清除session中的用户信息
    session.removeAttribute("user");
    // 重定向到登录页面
    response.sendRedirect("login.jsp");
%> 