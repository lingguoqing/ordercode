package com.qst.itoffer.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用户访问权限过滤器
 * @公司 青软实训
 * @作者 fengjj
 */
@WebFilter(
		urlPatterns = { "/manage/*" }, 
		servletNames = {"com.qst.itofferbacker.servlet.CompanyAddServlet", 
		"com.qst.itofferbacker.servlet.CompanyServlet", 
		"com.qst.itofferbacker.servlet.ResumeServlet",
		"com.qst.itofferbacker.servlet.UserAddServlet",
		"com.qst.itofferbacker.servlet.UserServlet"}, 
		initParams = { @WebInitParam(name = "loginPage", value = "login.jsp")})
public class AuthorityFilter implements Filter {
	private String loginPage;	
    public AuthorityFilter() {
    }
	public void init(FilterConfig fConfig) throws ServletException {
		// 获取当请求被拦截时转向的页面
		loginPage = fConfig.getInitParameter("loginPage");
		if (null == loginPage) {
			loginPage = "login.jsp";
		}
	}
	public void destroy() {
		this.loginPage = null;
	}
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		// 判断如果是登录请求则放行
		if("/UserServlet".equals(req.getServletPath()) && "type=login".equals(req.getQueryString())){
			chain.doFilter(request, response);
			return;
		}
		// 判断被拦截的请求用户是否处于登录状态，若未登录，返回登录页面
		if (session.getAttribute("SESSION_USER") == null) {
			resp.sendRedirect(req.getContextPath() + "/" + loginPage);
		} else {
			chain.doFilter(request, response);
		}
	}
}