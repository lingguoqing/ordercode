<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="java.util.List,com.qst.itoffer.bean.Company" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<%
    List<Company> list = (List<Company>) request.getAttribute("list");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>企业列表</title>
    <base href="<%=basePath%>"/>
    <link href="css/manageadmin.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="place"><span>位置：</span>
    <ul class="placeul">
        <li><a href="#">首页</a></li>
        <li><a href="#">企业列表</a></li>
    </ul>
</div>
<div class="rightinfo">
    <div class="tools">
        <ul class="toolbar">
            <li class="click"><span><img src="images/t01.png"/></span><a href="manage/companyAdd.html">添加</a></li>
            <li class="click"><span><img src="images/t02.png"/></span><a href="#">修改</a></li>
            <li><span><img src="images/t03.png"/></span><a href="#">删除</a></li>
        </ul>
    </div>
    <table class="imgtable">
        <thead>
        <tr>
            <th><input name="" type="checkbox" value="" checked="checked"/></th>
            <th>企业名称</th>
            <th>企业所在地</th>
            <th>企业规模</th>
            <th>企业性质</th>
            <th>招聘状态</th>
            <th>显示排序</th>
            <th>浏览数</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%
            if (list != null) {
                for (Company c : list) {
        %>
        <tr height="50px">
            <td><input name="" type="checkbox" value=""/></td>
            <td><%=c.getCompanyName() %>
            </td>
            <td><%=c.getCompanyArea() %>
            </td>
            <td><%=c.getCompanySize() %>
            </td>
            <td><%=c.getCompanyType() %>
            </td>
            <td><%if (c.getCompanyState() == 1) { %>招聘中
                <%} else if (c.getCompanyState() == 2) { %>已暂停
                <%} else if (c.getCompanyState() == 3) { %>已结束<%} %></td>
            <td><%=c.getCompanySort() %>
            </td>
            <td><%=c.getCompanyViewnum() %>
            </td>
            <td><a href="CompanyServlet?type=updateSelect&companyId=<%=c.getCompanyId() %>" class="tablelink">修改</a>
                &nbsp;&nbsp;<a href="#" class="tablelink"> 删除</a></td>
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
</div>
</body>
</html>