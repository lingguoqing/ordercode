package com.communication.platform.communicationplatform.servlet;

import com.communication.platform.communicationplatform.dao.QuestionDao;
import com.communication.platform.communicationplatform.entity.Question;
import com.communication.platform.communicationplatform.entity.User;
import com.communication.platform.communicationplatform.util.MyBatisUtil;
import com.communication.platform.communicationplatform.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/ask")
public class AskServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/ask.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String description = req.getParameter("description");

        // 参数验证
        if (title == null || title.trim().isEmpty() || 
            description == null || description.trim().isEmpty()) {
            req.setAttribute("error", "标题和问题描述不能为空");
            req.getRequestDispatcher("/ask.jsp").forward(req, resp);
            return;
        }

        // 获取当前登录用户
        User loginUser = SecurityUtil.getLoginUser(req.getSession());
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 创建问题
        Question question = new Question();
        question.setUserId(loginUser.getId());
        question.setTitle(title.trim());
        question.setDescription(description.trim());
        question.setCreateTime(LocalDateTime.now());
        question.setViewCount(0);

        // 保存问题
        QuestionDao questionDao = MyBatisUtil.getSqlSession().getMapper(QuestionDao.class);
        questionDao.insert(question);

        // 重定向到问题详情页
        resp.sendRedirect(req.getContextPath() + "/question?id=" + question.getId());
    }
} 