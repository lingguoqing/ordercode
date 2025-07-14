<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>职位列表</title>
    <base href="<%=basePath%>"/>
    <link href="css/manageadmin.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="place"> <span>位置：</span>
    <ul class="placeul">
        <li><a href="manage/index.html">首页</a></li>
        <li>职位列表</li>
    </ul>
</div>
<div class="rightinfo">
    <div class="tools">
        <ul class="toolbar">
            <li class="click"><span><img src="images/t01.png" /></span>
                <a href="manage/jobAdd.html">添加</a></li>
        </ul>
        <jsp:include page="jobSearch.jsp"></jsp:include>
    </div>
    <table class="imgtable">
        <thead>
        <tr>
            <th>职位名称</th>
            <th>所属企业</th>
            <th>招聘数</th>
            <th>申请数</th>
            <th>结束日期</th>
            <th>招聘状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${requestScope.joblist}" var="job">
            <tr height="50px">
                <td>${job.jobName }</td>
                <td>${job.company.companyName }</td>
                <td>${job.jobHiringnum }</td>
                <td>${job.applyNum }</td>
                <td>${job.jobEnddate }</td>
                <td><c:if test="${job.jobState == 1}">招聘中  </c:if>
                    <c:if test="${job.jobState == 2}">已暂停</c:if>
                    <c:if test="${job.jobState == 3}">已结束</c:if></td>
                <td ><a href="#" class="tablelink">修改</a> &nbsp;&nbsp;
                    <a href="#" class="tablelink"> 删除</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
