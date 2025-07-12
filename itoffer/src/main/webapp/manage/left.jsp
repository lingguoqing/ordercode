<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>功能菜单</title>
</head>
<body style="background:#f0f9fd;">
<div class="lefttop"><span></span>功能菜单</div>
<dl class="leftmenu">
    <%--仅当用户角色为管理员或企业用户时可查看 --%>
    <c:if test="${sessionScope.SESSION_USER.userRole == 3 ||
         sessionScope.SESSION_USER.userRole == 2}">
        <dd>
            <div class="title"><span><img src="../images/leftico01.png"/></span>
                企业职位管理
            </div>
            <ul class="menuson">
                <li><cite></cite><a href="jobApplyList.jsp" target="rightFrame">职位申请查看</a></li>
                <li><cite></cite><a href="../JobServlet?type=list" target="rightFrame">职位管理</a></li>
                <li><cite></cite><a href="../CompanyServlet?type=list"
                                    target="rightFrame">企业管理</a><i></i></li>
            </ul>
        </dd>
    </c:if>
    <%--所有登录用户角色均可查看 --%>
    <c:if test="${sessionScope.SESSION_USER.userRole == 1 ||
    sessionScope.SESSION_USER.userRole == 2 ||
    sessionScope.SESSION_USER.userRole == 3}">
        <dd>
            <div class="title"><span><img src="../images/leftico02.png"/></span>
                简历管理
            </div>
            <ul class="menuson">
                <li><cite></cite><a href="../ResumeServlet?type=list" target="rightFrame"> 简历查询</a><i></i></li>
            </ul>
        </dd>
    </c:if>
    <%--仅系统管理员角色可查看 --%>
    <c:if test="${sessionScope.SESSION_USER.userRole == 3 }">
        <dd>
            <div class="title"><span><img src="../images/leftico03.png"/></span>
                用户管理
            </div>
            <ul class="menuson">
                <li><cite></cite><a href="userList.jsp" target="rightFrame">
                    用户管理</a><i></i></li>
            </ul>
        </dd>
        <dd>
            <div class="title"><span><img src="../images/leftico04.png"/></span>
                系统管理
            </div>
            <ul class="menuson">
                <li><cite></cite><a href="userOnline.jsp" target="rightFrame">在线用户</a></li>
            </ul>
        </dd>
    </c:if>
    <%--所有用户角色均可查看 --%>
    <dd>
        <div class="title"><span><img src="../images/leftico04.png"/></span>
            <a href="#" target="rightFrame">密码修改</a></div>
    </dd>
</dl>
</body>
</html>