<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>职位搜索</title>
    <base href="<%=basePath%>"/>
    <link href="css/manageadmin.css" rel="stylesheet" type="text/css" />
</head>
<body>
<form action="JobServlet" method="post">
    <ul class="seachform">
        <li>
            <div class="vocation">
                所属企业： <select class="select3" name="companyId">
                <option value="0">全部企业</option>
                <c:forEach items="${requestScope.companylist}" var="company">
                    <option value="${company.companyId }">${company.companyName }</option>
                </c:forEach>
            </select>
            </div>
        </li>
        <li>
            职位名称：<input type="text" class="scinput" name="jobName"/>
        </li>
        <li>
            <input type="hidden" name="type" value="query"/>
            <input name="" type="submit" class="scbtn" value="查询"/>
        </li>
    </ul>
</form>
</body>
</html>