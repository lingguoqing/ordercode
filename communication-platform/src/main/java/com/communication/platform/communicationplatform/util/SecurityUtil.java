package com.communication.platform.communicationplatform.util;

import com.communication.platform.communicationplatform.entity.User;

import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {
    public static String md5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void setLoginUser(HttpSession session, User user) {
        // 不要将密码存入session
        user.setPassword(null);
        session.setAttribute("loginUser", user);
    }
    
    public static User getLoginUser(HttpSession session) {
        return (User) session.getAttribute("loginUser");
    }
    
    public static void removeLoginUser(HttpSession session) {
        session.removeAttribute("loginUser");
    }
} 