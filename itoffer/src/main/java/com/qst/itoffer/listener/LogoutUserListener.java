package com.qst.itoffer.listener;
import com.qst.itoffer.bean.User;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Enumeration;
import java.util.Map;

/**
* 在线用户退出监听器
* @公司 青软实训
* @作者 fengjj 
*/ 
@WebListener
public class LogoutUserListener implements HttpSessionListener {
    public void sessionDestroyed(HttpSessionEvent event)  {
        // 判断监听到的是否为记录用户信息的会话域属性
        Enumeration<String> attrnames = event.getSession().getAttributeNames();
        while(attrnames.hasMoreElements()){
            System.out.println(attrnames.nextElement());
            if("SESSION_USER".equals(attrnames.nextElement())){
                // 获取会话域属性值：当前登录的用户对象
                User user = (User) event.getSession().getAttribute("SESSION_USER");
                // 获取应用上下文对象
                ServletContext application =
                event.getSession().getServletContext();
                // 获取存放在应用域属性中的在线用户信息列表
                Map<String,User> onlineUserMap =
                 (Map<String,User>)application.getAttribute("ONLINE_USER");
                if(onlineUserMap != null){
                    // 将当前登录的用户对象移出应用域属性：在线用户信息列表
                    onlineUserMap.remove(user.getUserLogname());
                    application.setAttribute("ONLINE_USER", onlineUserMap);
                }
            }
         }
    }
}