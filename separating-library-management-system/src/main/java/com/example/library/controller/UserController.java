package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // 返回 templates/register.html
    }

    @PostMapping("/register")
    public String handleRegister(User user, Model model) {
        boolean success = userService.register(user);
        if (success) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "用户名已存在");
            return "register";
        }
    }

    @GetMapping({"/", "/login"})
    public String showLoginPage() {
        return "login"; // 返回 templates/login.html
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            if ("admin".equals(user.getRole())) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/user/dashboard";
        } else {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/user/profile")
    public String showProfilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", userService.findById(user.getId()));
        return "user_profile"; // 返回 templates/user_profile.html
    }

    @PostMapping("/user/profile")
    public String handleProfileUpdate(User user, @RequestParam String newPassword, HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        User existingUser = userService.findById(sessionUser.getId());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        if (newPassword != null && !newPassword.isEmpty()) {
            existingUser.setPassword(newPassword); // TODO: Add password encoding
        }
        userService.updateUser(existingUser);
        model.addAttribute("message", "个人信息更新成功");
        model.addAttribute("user", existingUser);
        return "user_profile";
    }
} 