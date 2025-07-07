package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.service.BookService;
import com.example.library.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    // 中间件：检查管理员权限
    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "admin".equals(user.getRole());
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        return "admin_dashboard"; // templates/admin_dashboard.html
    }

    // 用户管理
    @GetMapping("/users")
    public String listUsers(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin_users"; // templates/admin_users.html
    }

    // 图书管理
    @GetMapping("/books")
    public String listBooks(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        List<Book> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        return "admin_books"; // templates/admin_books.html
    }

    @GetMapping("/books/add")
    public String showAddBookForm(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("book", new Book());
        return "admin_book_form"; // templates/admin_book_form.html
    }

    @PostMapping("/books/save")
    public String saveBook(Book book, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/login";
        if (book.getId() == null) {
            bookService.addBook(book);
            redirectAttributes.addFlashAttribute("message", "图书添加成功！");
        } else {
            bookService.updateBook(book);
            redirectAttributes.addFlashAttribute("message", "图书更新成功！");
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/books/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        Book book = bookService.findBookById(id);
        model.addAttribute("book", book);
        return "admin_book_form";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/login";
        boolean deleted = bookService.deleteBook(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("message", "图书删除成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", "还有用户没有归还书籍");
        }
        return "redirect:/admin/books";
    }
} 