<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.communication.platform.communicationplatform.entity.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.communication.platform.communicationplatform.entity.User" %>
<%@ page import="com.communication.platform.communicationplatform.util.SecurityUtil" %>
<% 
    List<Question> questions = (List<Question>) request.getAttribute("questions");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    User loginUser = SecurityUtil.getLoginUser(session);
%>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="首页"/>
</jsp:include>

<style>
    .question-list {
        background: white;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        padding: 20px;
    }
    .question-item {
        padding: 20px;
        border-bottom: 1px solid #eee;
    }
    .question-item:last-child {
        border-bottom: none;
    }
    .question-title {
        font-size: 18px;
        margin-bottom: 10px;
    }
    .question-title a {
        color: #333;
        text-decoration: none;
    }
    .question-title a:hover {
        color: #4CAF50;
    }
    .question-meta {
        font-size: 14px;
        color: #666;
    }
    .question-meta span {
        margin-right: 15px;
    }
    .question-description {
        margin: 10px 0;
        color: #666;
        font-size: 14px;
        line-height: 1.6;
    }
    .empty-message {
        text-align: center;
        padding: 40px;
        color: #666;
    }
    .login-tip {
        text-align: center;
        padding: 20px;
        background: #f8f8f8;
        border-radius: 4px;
        margin-bottom: 20px;
    }
    .login-tip a {
        color: #4CAF50;
        text-decoration: none;
        font-weight: bold;
    }
    .login-tip a:hover {
        text-decoration: underline;
    }
    .ask-tip {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
    }
    .ask-tip h2 {
        margin: 0;
        color: #333;
    }
</style>

<% if (loginUser == null) { %>
    <div class="login-tip">
        想要提问或回答？请先 <a href="${pageContext.request.contextPath}/login">登录</a> 或 
        <a href="${pageContext.request.contextPath}/register">注册</a>
    </div>
<% } %>

<div class="ask-tip">
    <h2>最新问题</h2>
    <% if (loginUser != null) { %>
        <a href="${pageContext.request.contextPath}/ask" class="btn-ask">我要提问</a>
    <% } %>
</div>

<div class="question-list">
    <% if (questions != null && !questions.isEmpty()) { %>
        <% for (Question question : questions) { %>
            <div class="question-item">
                <div class="question-title">
                    <a href="${pageContext.request.contextPath}/question?id=<%=question.getId()%>">
                        <%=question.getTitle()%>
                    </a>
                </div>
                <div class="question-description">
                    <%=question.getDescription()%>
                </div>
                <div class="question-meta">
                    <span>提问者：<%=question.getUsername()%></span>
                    <span>浏览：<%=question.getViewCount()%></span>
                    <span>时间：<%=question.getCreateTime().format(formatter)%></span>
                </div>
            </div>
        <% } %>
    <% } else { %>
        <div class="empty-message">
            暂无问题
        </div>
    <% } %>
</div>

<jsp:include page="/common/footer.jsp"/>