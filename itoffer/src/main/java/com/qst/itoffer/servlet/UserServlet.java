package com.qst.itoffer.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.qst.itoffer.dao.UserDAO;
import com.qst.itoffer.bean.User;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UserServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String type = request.getParameter("type");
        HttpSession session = request.getSession();
        if ("login".equals(type)) {
            String sessionValidateCode = (String) session.getAttribute("successCode");
            if (sessionValidateCode == null || sessionValidateCode.equals("false")) {
                request.setAttribute("msg", "请先点击验证");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            String userLogname = request.getParameter("userLogname");
            String userPwd = request.getParameter("userPwd");
            UserDAO dao = new UserDAO();
            User user = dao.login(userLogname, userPwd);
            if (user != null) {
                request.getSession().setAttribute("SESSION_USER", user);
                response.sendRedirect("manage/main.jsp");
            } else {
                request.setAttribute("msg", "请检查用户名或密码是否正确");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;

            }
        } else if ("logout".equals(type)) { // 用户退出
            // 用户本次会话失效
            request.getSession().invalidate();
            // 返回登陆页面
            out.print("<script type='text/javascript'>");
            out.print("window.location='login.html';");
            out.print("</script>");
        } else if ("Code".equals(type)) {
            String xs = request.getParameter("x");
            String ys = request.getParameter("y");
            Integer x = Integer.parseInt(xs);
            Integer y = Integer.parseInt(ys);
            System.out.println(" checked x:" + xs + "y:" + ys);
            int[] xy = (int[]) session.getAttribute("xy");
            if (x > xy[0] - 10 && x < xy[0] + 50 && y > xy[1] - 50 && y < xy[1] + 10) {
                session.setAttribute("successCode", "true");
                session.setAttribute("errMsg", null);
                out.print("success");
                return;
            }
            session.setAttribute("successCode", "false");
            session.setAttribute("errMsg", "请重新点击");
            session.setAttribute("Character", null);
            out.print("faild");
            return;
        } else if ("updateSelect".equals(type)) { // 用户信息修改预查询
            int uid = Integer.parseInt(request.getParameter("userId"));
            UserDAO dao = new UserDAO();
            User user = dao.selectById(uid);
            request.setAttribute("user", user);
            request.getRequestDispatcher("manage/userUpdate.jsp")
                    .forward(request, response);
        } else if ("deleteSelect".equals(type)) {
            int uid = Integer.parseInt(request.getParameter("userId"));
            UserDAO dao = new UserDAO();
            dao.delete(uid);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("success");
        }
    }
}