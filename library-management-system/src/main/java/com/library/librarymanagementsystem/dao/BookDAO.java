package com.library.librarymanagementsystem.dao;

import com.library.librarymanagementsystem.entity.Book;
import com.library.librarymanagementsystem.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    
    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublicationDate(rs.getDate("publication_date"));
                book.setIsbn(rs.getString("isbn"));
                book.setQuantity(rs.getInt("quantity"));
                books.add(book);
            }
        }
        return books;
    }
    
    public Book getBookById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublicationDate(rs.getDate("publication_date"));
                book.setIsbn(rs.getString("isbn"));
                book.setQuantity(rs.getInt("quantity"));
                return book;
            }
        }
        return null;
    }
    
    public void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, publisher, publication_date, isbn, quantity) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setDate(4, new java.sql.Date(book.getPublicationDate().getTime()));
            pstmt.setString(5, book.getIsbn());
            pstmt.setInt(6, book.getQuantity());
            
            pstmt.executeUpdate();
        }
    }
    
    public void updateBook(Book book) throws SQLException {
        String sql = "UPDATE books SET title=?, author=?, publisher=?, publication_date=?, isbn=?, quantity=? WHERE id=?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setDate(4, new java.sql.Date(book.getPublicationDate().getTime()));
            pstmt.setString(5, book.getIsbn());
            pstmt.setInt(6, book.getQuantity());
            pstmt.setInt(7, book.getId());
            
            pstmt.executeUpdate();
        }
    }
    
    public void deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id=?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Book> searchBooks(String title, String author, String publicationDate) throws SQLException {
        List<Book> books = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM books WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (title != null && !title.trim().isEmpty()) {
            sqlBuilder.append(" AND title LIKE ?");
            params.add("%" + title + "%");
        }
        if (author != null && !author.trim().isEmpty()) {
            sqlBuilder.append(" AND author = ?");
            params.add(author);
        }
        if (publicationDate != null && !publicationDate.trim().isEmpty()) {
            sqlBuilder.append(" AND publication_date = ?");
            params.add(java.sql.Date.valueOf(publicationDate));
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    pstmt.setString(i + 1, (String) param);
                } else if (param instanceof Date) {
                    pstmt.setDate(i + 1, (Date) param);
                }
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublicationDate(rs.getDate("publication_date"));
                book.setIsbn(rs.getString("isbn"));
                book.setQuantity(rs.getInt("quantity"));
                books.add(book);
            }
        }
        return books;
    }

    public Book findByTitle(String title) throws SQLException {
        String sql = "SELECT * FROM books WHERE title = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setPublisher(rs.getString("publisher"));
                    book.setPublicationDate(rs.getDate("publication_date"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setQuantity(rs.getInt("quantity"));
                    return book;
                } else {
                    return null;
                }
            }
        }
    }
} 