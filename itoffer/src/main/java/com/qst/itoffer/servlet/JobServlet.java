package com.qst.itoffer.servlet;

import com.qst.itoffer.dao.CompanyDAO;
import com.qst.itoffer.dao.JobDAO;
import com.qst.itoffer.bean.Company;
import com.qst.itoffer.bean.Job;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/JobServlet")
public class JobServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // 获取请求操作类型
        String type = request.getParameter("type");
        // 创建职位和企业DAO对象
        JobDAO jobdao = new JobDAO();
        CompanyDAO companydao = new CompanyDAO();
        // 职位信息查询页面所需的职位信息和企业信息的查询
        if ("list".equals(type)) {
            // 获得所有职位信息
            List<Job> joblist = jobdao.selectAll();
            // 获得所有企业名称和标识信息
            List<Company> companylist = companydao.selectAllCompanyName();
            request.setAttribute("joblist", joblist);
            request.setAttribute("companylist", companylist);
            request.getRequestDispatcher("manage/jobList.jsp")
                .forward(request,response);
            return;
        }else if("query".equals(type)){
            int companyId = Integer.parseInt(request.getParameter("companyId"));
            String jobName = request.getParameter("jobName");
            List<Job> joblist = jobdao.query(companyId,jobName);
            List<Company> companylist = companydao.selectAllCompanyName();
            request.setAttribute("joblist", joblist);
            request.setAttribute("companylist", companylist);
            request.getRequestDispatcher("manage/jobList.jsp").forward(request,
                    response);
            return;
        }
    }
}