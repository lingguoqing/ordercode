<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.communication.platform.communicationplatform.entity.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<% 
    List<Question> questions = (List<Question>) request.getAttribute("questions");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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
</style>

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
            暂无问题，快来提问吧！
        </div>
    <% } %>
</div>

<jsp:include page="/common/footer.jsp"/>