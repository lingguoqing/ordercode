package com.qst.itoffer.servlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qst.itoffer.bean.User;
import com.qst.itoffer.dao.UserDAO;

/**
 * 用户信息添加Servlet
 * @公司 青软实训
 * @作者 fengjj
 */
@WebServlet("/UserAddServlet")
public class UserAddServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    public UserAddServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求字符编码
        request.setCharacterEncoding("UTF-8");
        // 获取用户添加信息
        String userLogname = request.getParameter("userLogname");
        String userPwd = request.getParameter("userPwd");
        String userRealname = request.getParameter("userRealname");
        String userEmail = request.getParameter("userEmail");
        int userRole = (request.getParameter("userRole") == null) ? 3 : Integer.parseInt(request.getParameter("userRole"));
        int userState = (request.getParameter("userState") == null) ? 1: Integer.parseInt(request.getParameter("userState"));
        // 定义一个用来封装用户信息的JavaBean
        User user = new User(userLogname,userPwd,userRealname,userEmail,userRole,userState);
        // 用户信息添加
        UserDAO dao = new UserDAO();
        dao.save(user);
        // 添加成功，重定向到响应页面
        response.sendRedirect("manage/userList.jsp");
    }

}