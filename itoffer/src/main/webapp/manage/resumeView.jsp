<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
  String basePath = request.getScheme()+":/"+request.getServerName()+":"
          +request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>简历详情</title>
  <base href="<%=basePath%>">
  <link href="css/manageadmin.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="place"> <span>位置：</span>
  <ul class="placeul">
    <li><a href="index.html">首页</a></li>
    <li>简历详情</li>
  </ul>
</div>
<div class="formbody">
  <div class="usual">
    <div class="tabson">
      <ul class="forminfo">
        <li>
          <label>真实姓名：</label><label style="width: 200px;"><b style="color: #000">${requestScope.resume.realName }</b></label>
        </li>
        <li>
          <label>照片：</label>
          <img src="/upload/resumePhoto/${requestScope.resume.headShot }"
               width="100px" height="100px">
        </li>
        <li>
          <label>性别：</label><label style="width: 200px;">
          <b style="color: #000">${requestScope.resume.gender }</b></label>
        </li>
        <li>
          <label>出生日期：</label><label style="width: 200px;">
          <b style="color: #000">${requestScope.resume.birthday }</b></label>
        </li>
        <li>
          <label>当前所在地：</label><label style="width: 200px;">
          <b style="color: #000">
            ${requestScope.resume.currentLoc }</b></label>
        </li>
        <li>
          <label>户口所在地：</label><label style="width: 200px;">
          <b style="color: #000">
            ${requestScope.resume.residentLoc }</b></label>
        </li>
        <li>
          <label>手机号：</label><label style="width: 200px;">
          <b style="color: #000">
            ${requestScope.resume.telephone }</b></label>
        </li>
        <li>
          <label>邮箱：</label><label style="width: 200px;">
          <b style="color: #000">${requestScope.resume.email }</b></label>
        </li>
        <li>
          <label>求职意向：</label><label style="width: 200px;">
          <b style="color: #000">
            ${requestScope.resume.jobIntension }</b></label>
        </li>
        <li>
          <label>工作经验：</label><label style="width: 200px;">
          <b style="color: #000">
            ${requestScope.resume.jobExperience }</b></label>
        </li>
      </ul>
    </div>
  </div>
</div>
</body>
</html>