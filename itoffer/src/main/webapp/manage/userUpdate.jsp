<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>修改用户</title>
    <base href="<%=basePath%>"/>
    <link href="css/manageadmin.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="place"><span>位置：</span>
    <ul class="placeul">
        <li><a href="#">首页</a></li>
        <li><a href="#">修改用户</a></li>
    </ul>
</div>
<div class="formbody">
    <div class="usual">

        <jsp:useBean id="user" class="com.qst.itoffer.bean.User" scope="request"></jsp:useBean>

        <form name="frm" action="manage/userUpdateOper.jsp" method="post">
            <div class="tabson">
                <ul class="forminfo">
                    <li>
                        <label>登录名称</label>
                        <input name="userLogname" value="<jsp:getProperty property='userLogname' name='user'/>"
                               readonly="readonly" type="text" class="dfinput" style="width:518px;" required="required"/>
                    </li>
                    <li>
                        <label>真实姓名<b>*</b></label>
                        <input name="userRealname"
                               value="<jsp:getProperty property='userRealname' name='user'/>" type="text"
                               class="dfinput" style="width:518px;" required="required"/>
                    </li>
                    <li>
                        <label>邮箱<b>*</b></label>
                        <input name="userEmail" value="<jsp:getProperty property='userEmail' name='user'/>"
                               type="text" class="dfinput" style="width:518px;" required="required"/>
                    </li>
                    <li>
                        <label>用户角色<b>*</b></label>
                        <div class="vocation">
                            <select name="userRole" class="select3">
                                <option value="1" <%if (user.getUserRole() == 1) { %> selected="selected"<%} %>>
                                    普通用户
                                </option>
                                <option value="2" <%if (user.getUserRole() == 2) { %> selected="selected"<%} %>>
                                    企业管理员
                                </option>
                                <option value="3" <%if (user.getUserRole() == 3) { %> selected="selected"<%} %>>
                                    系统管理员
                                </option>
                            </select>
                        </div>
                    </li>
                    <li>
                        <label>用户状态<b>*</b></label>
                        <input name="userState" type="radio" value="1"
                               <%if(user.getUserState() == 1) { %>checked="checked"<%} %>/>启用
                        <input name="userState" type="radio" value="0"
                               <%if(user.getUserState() == 0) { %>checked="checked"<%} %>/>禁用
                    </li>
                    <li><label>&nbsp;</label>
                        <input name="userId" value="<jsp:getProperty property='userId' name='user'/>" type="hidden">
                        <input name="" type="submit" class="btn" value="修改"/>
                    </li>
                </ul>
            </div>
        </form>
    </div>
</div>
</body>
</html>