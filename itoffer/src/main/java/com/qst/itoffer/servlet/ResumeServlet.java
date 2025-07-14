package com.qst.itoffer.servlet;

import com.qst.itoffer.bean.ResumeBasicinfo;
import com.qst.itoffer.dao.ResumeDAO;
import com.qst.itoffer.bean.PageBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 简历信息管理Servlet
 * @公司 青软实训
 * @作者 fengjj
 */
@WebServlet("/ResumeServlet")
public class ResumeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求字符编码
        request.setCharacterEncoding("UTF-8");
        String type = request.getParameter("type");
        if("list".equals(type)){
            // 获取请求页码
            int pageNo =
                Integer.parseInt(request.getParameter("pageNo")==null?"1":
                request.getParameter("pageNo"));
            ResumeDAO dao = new ResumeDAO();
            // 查询总记录数
            int recordCount = dao.getRecordCount();
            // 查询请求页码数据
            List<ResumeBasicinfo> list = dao.getPageList(pageNo, 10);
            // 将分页信息封装到PageBean对象中
            PageBean<ResumeBasicinfo> pageBean =
                 new PageBean<ResumeBasicinfo>();
            pageBean.setPageNo(pageNo);
            pageBean.setRecordCount(recordCount);
            pageBean.setPageData(list);
            // 将分页数据对象PageBean对象存入请求域属性中
            request.setAttribute("pageBean", pageBean);
            // 将请求转发到简历列表页面
            request.getRequestDispatcher("manage/resumeList.jsp")
                .forward(request, response);
        }else if("select".equals(type)){
            int resumeId = Integer.parseInt(request.getParameter("resumeId"));
            ResumeDAO dao = new ResumeDAO();
            ResumeBasicinfo resume = dao.selectBasicinfoByID(resumeId);
            request.setAttribute("resume", resume);
            request.getRequestDispatcher("manage/resumeView.jsp")
                    .forward(request, response);
        }
    }
}