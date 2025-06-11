<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.library.librarymanagementsystem.dao.BookDAO" %>
<%@ page import="com.library.librarymanagementsystem.entity.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
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
    <title>图书管理系统 - 图书列表</title>
    <link href="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>图书列表</h2>
            <div>
                <span class="me-3">欢迎，<%= session.getAttribute("user") != null ? ((User)session.getAttribute("user")).getUsername() : "" %></span>
                <a href="logout.jsp" class="btn btn-outline-danger">退出登录</a>
            </div>
        </div>
        
        <div class="mb-3">
            <a href="add-book.jsp" class="btn btn-primary">添加新书</a>
        </div>
        
        <form action="book-list.jsp" method="get" class="mb-4">
            <div class="row g-3 align-items-end">
                <div class="col-md-4">
                    <label for="searchTitle" class="form-label">书名</label>
                    <input type="text" class="form-control" id="searchTitle" name="searchTitle" value="<%= request.getParameter("searchTitle") != null ? request.getParameter("searchTitle") : "" %>">
                </div>
                <div class="col-md-3">
                    <label for="searchAuthor" class="form-label">作者</label>
                    <input type="text" class="form-control" id="searchAuthor" name="searchAuthor" value="<%= request.getParameter("searchAuthor") != null ? request.getParameter("searchAuthor") : "" %>">
                </div>
                <div class="col-md-3">
                    <label for="searchDate" class="form-label">出版日期</label>
                    <input type="date" class="form-control" id="searchDate" name="searchDate" value="<%= request.getParameter("searchDate") != null ? request.getParameter("searchDate") : "" %>">
                </div>
                <div class="col-md-2 d-flex gap-2">
                    <button type="submit" class="btn btn-info">查询</button>
                    <a href="book-list.jsp" class="btn btn-secondary">清空</a>
                </div>
            </div>
        </form>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>书名</th>
                    <th>作者</th>
                    <th>出版社</th>
                    <th>出版日期</th>
                    <th>ISBN</th>
                    <th>库存数量</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <%
                    BookDAO bookDAO = new BookDAO();
                    try {
                        List<Book> books;
                        String searchTitle = request.getParameter("searchTitle");
                        String searchAuthor = request.getParameter("searchAuthor");
                        String searchDate = request.getParameter("searchDate");

                        if ((searchTitle != null && !searchTitle.trim().isEmpty()) ||
                            (searchAuthor != null && !searchAuthor.trim().isEmpty()) ||
                            (searchDate != null && !searchDate.trim().isEmpty())) {
                            books = bookDAO.searchBooks(searchTitle, searchAuthor, searchDate);
                        } else {
                            books = bookDAO.getAllBooks();
                        }

                        for (Book book : books) {
                %>
                <tr>
                    <td><%= book.getId() %></td>
                    <td><%= book.getTitle() %></td>
                    <td><%= book.getAuthor() %></td>
                    <td><%= book.getPublisher() %></td>
                    <td><%= book.getPublicationDate() %></td>
                    <td><%= book.getIsbn() %></td>
                    <td><%= book.getQuantity() %></td>
                    <td>
                        <a href="edit-book.jsp?id=<%= book.getId() %>" class="btn btn-sm btn-warning">编辑</a>
                        <a href="delete-book.jsp?id=<%= book.getId() %>" class="btn btn-sm btn-danger" 
                           onclick="return confirm('确定要删除这本书吗？')">删除</a>
                    </td>
                </tr>
                <%
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                %>
            </tbody>
        </table>
    </div>
    
    <script src="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/5.1.3/js/bootstrap.bundle.min.js"></script>
</body>
</html> 