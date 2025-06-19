<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.communication.platform.communicationplatform.entity.Question" %>
<%@ page import="com.communication.platform.communicationplatform.entity.Answer" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.communication.platform.communicationplatform.entity.User" %>
<%@ page import="com.communication.platform.communicationplatform.util.SecurityUtil" %>
<% 
    Question question = (Question) request.getAttribute("question");
    List<Answer> answers = (List<Answer>) request.getAttribute("answers");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    User loginUser = SecurityUtil.getLoginUser(session);
%>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="${question.title}"/>
</jsp:include>

<style>
    .question-detail, .answer-form, .answer-list {
        background: white;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        padding: 20px;
        margin-bottom: 20px;
    }
    .question-title {
        font-size: 24px;
        color: #333;
        margin-bottom: 15px;
    }
    .question-meta {
        color: #666;
        font-size: 14px;
        margin-bottom: 20px;
    }
    .question-meta span {
        margin-right: 15px;
    }
    .question-content {
        font-size: 16px;
        line-height: 1.6;
        margin-bottom: 20px;
        white-space: pre-wrap;
    }
    .answer-item {
        border-bottom: 1px solid #eee;
        padding: 20px 0;
    }
    .answer-item:last-child {
        border-bottom: none;
    }
    .answer-content {
        font-size: 14px;
        line-height: 1.6;
        margin-bottom: 10px;
        white-space: pre-wrap;
    }
    .answer-meta {
        color: #666;
        font-size: 12px;
    }
    .answer-meta span {
        margin-right: 15px;
    }
    textarea {
        width: 100%;
        height: 150px;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
        resize: vertical;
        margin-bottom: 15px;
    }
    .btn-submit {
        background-color: #4CAF50;
        color: white;
        padding: 10px 20px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }
    .btn-submit:hover {
        background-color: #45a049;
    }
    .answer-count {
        font-size: 18px;
        color: #333;
        margin-bottom: 20px;
        padding-bottom: 10px;
        border-bottom: 1px solid #eee;
    }
</style>

<div class="question-detail">
    <h1 class="question-title"><%=question.getTitle()%></h1>
    <div class="question-meta">
        <span>提问者：<%=question.getUsername()%></span>
        <span>浏览：<%=question.getViewCount()%></span>
        <span>时间：<%=question.getCreateTime().format(formatter)%></span>
    </div>
    <div class="question-content"><%=question.getDescription()%></div>
</div>

<div class="answer-list">
    <div class="answer-count"><%=answers.size()%> 个回答</div>
    <% if (answers != null && !answers.isEmpty()) { %>
        <% for (Answer answer : answers) { %>
            <div class="answer-item">
                <div class="answer-content"><%=answer.getContent()%></div>
                <div class="answer-meta">
                    <span>回答者：<%=answer.getUsername()%></span>
                    <span>时间：<%=answer.getCreateTime().format(formatter)%></span>
                </div>
            </div>
        <% } %>
    <% } %>
</div>

<% if (loginUser != null) { %>
    <div class="answer-form">
        <h3>我要回答</h3>
        <form action="${pageContext.request.contextPath}/question" method="post">
            <input type="hidden" name="questionId" value="<%=question.getId()%>">
            <textarea name="content" placeholder="请输入你的回答..." required></textarea>
            <div style="text-align: right;">
                <button type="submit" class="btn-submit">提交回答</button>
            </div>
        </form>
    </div>
<% } %>

<jsp:include page="/common/footer.jsp"/> 