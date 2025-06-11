<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.library.librarymanagementsystem.dao.BookDAO" %>
<%@ page import="com.library.librarymanagementsystem.entity.Book" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    request.setCharacterEncoding("UTF-8");
    
    String title = request.getParameter("title");
    String author = request.getParameter("author");
    String publisher = request.getParameter("publisher");
    String publicationDateStr = request.getParameter("publicationDate");
    String isbn = request.getParameter("isbn");
    int quantity = Integer.parseInt(request.getParameter("quantity"));
    
    Book book = new Book();
    book.setTitle(title);
    book.setAuthor(author);
    book.setPublisher(publisher);
    
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        book.setPublicationDate(sdf.parse(publicationDateStr));
    } catch (ParseException e) {
        e.printStackTrace();
    }
    
    book.setIsbn(isbn);
    book.setQuantity(quantity);
    
    BookDAO bookDAO = new BookDAO();
    try {
        // 检查书名是否已存在
        Book existingBook = bookDAO.findByTitle(title);
        if (existingBook != null) {
            request.setAttribute("errorMessage", "书名已存在，请尝试其他书名。");
            request.getRequestDispatcher("add-book.jsp").forward(request, response);
            return;
        }

        bookDAO.addBook(book);
        response.sendRedirect("book-list.jsp");
    } catch (SQLException e) {
        e.printStackTrace();
        request.setAttribute("errorMessage", "数据库操作失败，请稍后再试。");
        request.getRequestDispatcher("add-book.jsp").forward(request, response);
    }
%> 