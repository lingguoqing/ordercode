<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Q_ITOffer锐聘网后台管理系统</title>
    <link href="../css/manageadmin.css" rel="stylesheet" type="text/css"/>
</head>
<body style="background:url(../images/topbg.gif) repeat-x;">
<div class="topleft">
    <a href="main.html" target="_parent"><img src="../images/main_logo.png" title="系统首页" width="200px"
                                              style="margin-top: 30px;"/></a></div>

<div class="topright">
    <ul>
        <li><span><img src="../images/help.png" title="帮助" class="helpimg"/></span><a href="#">帮助</a></li>
        <li><a href="#">关于</a></li>
        <li><a href="../UserServlet?type=logout" target="_parent">退出</a></li>
    </ul>
    <div class="user"><span>${sessionScope.SESSION_USER.userRealname}</span></div>
</div>
<ul class="nav">
    <li><a href="http://www.itoffer.cn" target="_blank" class="selected"><img src="../images/globe.png"
                                                                              title="网站前台"/>
        <h2>网站前台</h2>
    </a></li>
    <li><a href="index.html" target="rightFrame"><img src="../images/home.png" title="后台首页"/>
        <h2>后台首页</h2>
    </a></li>
</ul>
</body>
</html>