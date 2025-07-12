<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="com.qst.itoffer.bean.Company" %>
<%
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
    Company company = (Company)request.getAttribute("company");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>企业信息更新</title>
    <base href="<%=basePath%>"/>
    <link href="css/manageadmin.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="ckeditor/ckeditor.js"></script>
    <script type="text/javascript">
        function validate(){
            if(document.getElementById("companyName").value == ""){
                alert("企业名称不能为空！");
                document.getElementById("companyName").focus();
                return false;
            }
            if(document.getElementById("companyArea").value == ""){
                alert("企业所在地不能为空！");
                document.getElementById("companyArea").focus();
                return false;
            }
            if(document.getElementById("companySize").value == ""){
                alert("企业规模不能为空！");
                document.getElementById("companySize").focus();
                return false;
            }
            if(document.getElementById("companyType").value == ""){
                alert("企业性质不能为空！");
                document.getElementById("companyType").focus();
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<div class="place"> <span>位置：</span>
    <ul class="placeul">
        <li><a href="index.html">首页</a></li>
        <li>企业信息更新</li>
    </ul>
</div>
<div class="formbody">
    <div class="usual">
        <form name="frm" action="CompanyServlet" method="post" enctype="multipart/form-data" onsubmit="return validate();">
            <div class="tabson">
                <ul class="forminfo">
                    <li>
                        <label>企业名称<b>*</b></label>
                        <input name="companyName" type="text" value="<%=company.getCompanyName() %>" id="companyName" class="dfinput" style="width:518px;"/>
                    </li>
                    <li>
                        <label>企业所在地<b>*</b></label>
                        <input name="companyArea" type="text" value="<%=company.getCompanyArea() %>" id="companyArea" class="dfinput" style="width:518px;"/>
                    </li>
                    <li>
                        <label>企业规模<b>*</b></label>
                        <input name="companySize" type="text" value="<%=company.getCompanySize() %>" id="companySize" class="dfinput" style="width:518px;"/>
                    </li>
                    <li>
                        <label>企业性质<b>*</b></label>
                        <input name="companyType" type="text" value="<%=company.getCompanyType() %>" id="companyType" class="dfinput" style="width:518px;"/>
                    </li>
                    <li>
                        <p>企业简介&nbsp;(不超过1000字)</p>
                        <textarea class="ckeditor" cols="50" id="companyBrief" name="companyBrief" rows="10"><%=company.getCompanyBrief() %></textarea>
                    </li>
                    <li>
                        <label>企业招聘状态</label>
                        <div class="vocation">
                            <select name="companyState" class="select3">
                                <option value="1" <%if(company.getCompanyState()==1){ %>selected="selected"<%} %>>招聘中</option>
                                <option value="2" <%if(company.getCompanyState()==2){ %>selected="selected"<%} %>>已暂停</option>
                                <option value="3" <%if(company.getCompanyState()==3){ %>selected="selected"<%} %>>已结束</option>
                            </select>
                        </div>
                    </li>
                    <li>
                        <label>显示排序<b></b></label>
                        <input name="companySort" type="text" value="<%=company.getCompanySort() %>" class="dfinput" style="width:100px;"/>
                    </li>
                    <li>
                        <label>宣传图片<b>*</b></label>
                        <img src="/upload/companyPic/<%=company.getCompanyPic() %>" height="100px" width="100px">
                        <input name="companyPic" id="companyPic" type="file" class="dfinput" style="width:300px; margin-right:5px;"/>
                        <input name="companyOldPic" value="<%=company.getCompanyPic()%>" type="hidden">
                    </li>
                    <li>
                        <input name="companyId" value="<%=company.getCompanyId() %>" type="hidden">
                        <input name="type" value="update" type="hidden">
                        <input name="" type="submit" class="btn" value="修改"/>
                    </li>
                </ul>
            </div>
        </form>
    </div>
</div>
</body>
</html>