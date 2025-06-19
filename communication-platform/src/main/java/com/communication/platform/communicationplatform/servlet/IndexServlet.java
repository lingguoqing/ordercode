package com.communication.platform.communicationplatform.servlet;

import com.communication.platform.communicationplatform.dao.QuestionDao;
import com.communication.platform.communicationplatform.entity.Question;
import com.communication.platform.communicationplatform.util.MyBatisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取问题列表
        QuestionDao questionDao = MyBatisUtil.getSqlSession().getMapper(QuestionDao.class);
        List<Question> questions = questionDao.findAll();
        
        // 将问题列表传递给JSP
        req.setAttribute("questions", questions);
        
        // 转发到首页
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
} 