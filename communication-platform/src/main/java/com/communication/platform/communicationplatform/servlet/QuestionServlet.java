package com.communication.platform.communicationplatform.servlet;

import com.communication.platform.communicationplatform.dao.AnswerDao;
import com.communication.platform.communicationplatform.dao.QuestionDao;
import com.communication.platform.communicationplatform.entity.Answer;
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
import java.util.List;

@WebServlet("/question")
public class QuestionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        try {
            Long id = Long.parseLong(idStr);
            QuestionDao questionDao = MyBatisUtil.getSqlSession().getMapper(QuestionDao.class);
            Question question = questionDao.findById(id);

            if (question == null) {
                resp.sendRedirect(req.getContextPath() + "/");
                return;
            }

            // 更新浏览次数
            question.setViewCount(question.getViewCount() + 1);
            questionDao.updateViewCount(question.getId(), question.getViewCount());

            // 获取问题的回答列表
            AnswerDao answerDao = MyBatisUtil.getSqlSession().getMapper(AnswerDao.class);
            List<Answer> answers = answerDao.findByQuestionId(id);

            req.setAttribute("question", question);
            req.setAttribute("answers", answers);
            req.getRequestDispatcher("/question.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 处理回答问题的请求
        String questionIdStr = req.getParameter("questionId");
        String content = req.getParameter("content");

        if (questionIdStr == null || questionIdStr.trim().isEmpty() || 
            content == null || content.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/question?id=" + questionIdStr);
            return;
        }

        try {
            Long questionId = Long.parseLong(questionIdStr);
            
            // 获取当前登录用户
            User loginUser = SecurityUtil.getLoginUser(req.getSession());
            if (loginUser == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            // 创建回答
            Answer answer = new Answer();
            answer.setQuestionId(questionId);
            answer.setUserId(loginUser.getId());
            answer.setContent(content.trim());
            answer.setCreateTime(LocalDateTime.now());

            // 保存回答
            AnswerDao answerDao = MyBatisUtil.getSqlSession().getMapper(AnswerDao.class);
            answerDao.insert(answer);

            // 重定向回问题详情页
            resp.sendRedirect(req.getContextPath() + "/question?id=" + questionId);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }
} 