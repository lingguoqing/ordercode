package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.Borrow;
import com.example.library.model.Subscribe;
import com.example.library.model.User;
import com.example.library.service.BookService;
import com.example.library.service.BorrowService;
import com.example.library.service.SubscribeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowService borrowService;
    
    @Autowired
    private SubscribeService subscribeService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        List<Book> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        return "user_dashboard"; // templates/user_dashboard.html
    }

    @GetMapping("/borrow/{bookId}")
    public String borrowBook(@PathVariable Long bookId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        boolean success = borrowService.borrowBook(user.getId(), bookId);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "借阅成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", "借阅失败，库存不足或已借阅。");
        }
        return "redirect:/user/dashboard";
    }

    @GetMapping("/return/{bookId}")
    public String returnBook(@PathVariable Long bookId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        
        boolean success = borrowService.returnBook(user.getId(), bookId);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "归还成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", "归还失败，未找到您的借阅记录。");
        }
        return "redirect:/user/myborrows";
    }

    @GetMapping("/myborrows")
    public String showMyBorrows(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Borrow> borrows = borrowService.findBorrowsByUserId(user.getId());
        model.addAttribute("borrows", borrows);
        // 为了方便显示书名，可以进一步处理
        return "my_borrows"; // templates/my_borrows.html
    }
    
    @GetMapping("/subscribe/{bookId}")
    public String subscribeBook(@PathVariable Long bookId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        boolean success = subscribeService.subscribeBook(user.getId(), bookId);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "订阅成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", "订阅失败，您已订阅该图书。");
        }
        return "redirect:/user/dashboard";
    }
    
    @GetMapping("/mysubscriptions")
    public String showMySubscriptions(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Subscribe> subscribes = subscribeService.findSubscriptionsByUserId(user.getId());
        model.addAttribute("subscribes", subscribes);
        return "my_subscriptions"; // templates/my_subscriptions.html
    }
} 