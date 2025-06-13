<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.library.librarymanagementsystem.dao.BookDAO" %>
<%@ page import="com.library.librarymanagementsystem.entity.Book" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.library.librarymanagementsystem.entity.User" %>
<%
    // 检查用户是否已登录
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>图书管理系统 - 编辑图书</title>
    <link href="assets/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>编辑图书</h2>
            <div>
                <span class="me-3">欢迎，<%= session.getAttribute("user") != null ? ((User)session.getAttribute("user")).getUsername() : "" %></span>
                <a href="logout.jsp" class="btn btn-outline-danger">退出登录</a>
            </div>
        </div>
        
        <%
            int id = Integer.parseInt(request.getParameter("id"));
            BookDAO bookDAO = new BookDAO();
            Book book = null;
            try {
                book = bookDAO.getBookById(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            if (book != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String publicationDate = sdf.format(book.getPublicationDate());
        %>
        <form action="process-edit-book.jsp" method="post">
            <input type="hidden" name="id" value="<%= book.getId() %>">
            <div class="mb-3">
                <label for="title" class="form-label">书名</label>
                <input type="text" class="form-control" id="title" name="title" value="<%= book.getTitle() %>" required>
            </div>
            <div class="mb-3">
                <label for="author" class="form-label">作者</label>
                <input type="text" class="form-control" id="author" name="author" value="<%= book.getAuthor() %>" required>
            </div>
            <div class="mb-3">
                <label for="publisher" class="form-label">出版社</label>
                <input type="text" class="form-control" id="publisher" name="publisher" value="<%= book.getPublisher() %>" required>
            </div>
            <div class="mb-3">
                <label for="publicationDate" class="form-label">出版日期</label>
                <input type="date" class="form-control" id="publicationDate" name="publicationDate" value="<%= publicationDate %>" required>
            </div>
            <div class="mb-3">
                <label for="isbn" class="form-label">ISBN</label>
                <input type="text" class="form-control" id="isbn" name="isbn" value="<%= book.getIsbn() %>" required>
            </div>
            <div class="mb-3">
                <label for="quantity" class="form-label">库存数量</label>
                <input type="number" class="form-control" id="quantity" name="quantity" value="<%= book.getQuantity() %>" required min="0">
            </div>
            <button type="submit" class="btn btn-primary">保存</button>
            <a href="book-list.jsp" class="btn btn-secondary">返回</a>
        </form>
        <%
            } else {
        %>
        <div class="alert alert-danger">
            未找到指定的图书
        </div>
        <a href="book-list.jsp" class="btn btn-secondary">返回</a>
        <%
            }
        %>
    </div>
    
    <script src="assets/bootstrap.bundle.min.js"></script>
</body>
</html> 