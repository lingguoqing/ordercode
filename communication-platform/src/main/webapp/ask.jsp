<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="提问"/>
</jsp:include>

<style>
    .ask-form {
        background: white;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        padding: 20px;
    }
    .form-group {
        margin-bottom: 20px;
    }
    label {
        display: block;
        margin-bottom: 8px;
        color: #333;
        font-weight: bold;
    }
    input[type="text"], textarea {
        width: 100%;
        padding: 8px;
        border: 1px solid #ddd;
        border-radius: 4px;
        box-sizing: border-box;
        font-size: 14px;
    }
    textarea {
        height: 200px;
        resize: vertical;
    }
    .btn-submit {
        background-color: #4CAF50;
        color: white;
        padding: 10px 20px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 16px;
    }
    .btn-submit:hover {
        background-color: #45a049;
    }
    .error {
        color: red;
        margin-bottom: 15px;
    }
</style>

<div class="ask-form">
    <h2>发布问题</h2>
    <% if (request.getAttribute("error") != null) { %>
        <div class="error">${error}</div>
    <% } %>
    <form action="${pageContext.request.contextPath}/ask" method="post">
        <div class="form-group">
            <label for="title">问题标题：</label>
            <input type="text" id="title" name="title" required 
                   placeholder="请输入问题标题（简短、明确）">
        </div>
        <div class="form-group">
            <label for="description">问题描述：</label>
            <textarea id="description" name="description" required
                      placeholder="请详细描述你的问题，包括：&#10;1. 问题的具体表现&#10;2. 你已经尝试过的解决方法&#10;3. 相关的代码或错误信息"></textarea>
        </div>
        <div style="text-align: center;">
            <button type="submit" class="btn-submit">发布问题</button>
        </div>
    </form>
</div>

<jsp:include page="/common/footer.jsp"/> 