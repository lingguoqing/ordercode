<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.library.librarymanagementsystem.dao.BookDAO" %>
<%@ page import="java.sql.SQLException" %>
<%
    int id = Integer.parseInt(request.getParameter("id"));
    BookDAO bookDAO = new BookDAO();
    
    try {
        bookDAO.deleteBook(id);
        response.sendRedirect("book-list.jsp");
    } catch (SQLException e) {
        e.printStackTrace();
        response.sendRedirect("error.jsp");
    }
%> 