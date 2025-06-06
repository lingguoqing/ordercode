package com.attendance.attendancedatasynchronization.utils;

import sun.security.provider.MD5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUtil {

    public static  String getSign(String path, String key, String secret) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        System.out.println(timeStamp);
//        {App-Sig} = md5({Path}{App-Timestamp}{App-Key}{App-Secret})，
//        String input = "/v2.0/employee15323159063643c5ee48d0b7d48c565ded5353c5ee48d0b7d48c591b8f430";
        String sign = getMD5Hash(path + timeStamp + key + secret);
        return sign;
    }

    private static String getMD5Hash(String input) {
        try {
            // 获取MD5加密算法实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 计算MD5哈希值
            byte[] messageDigest = md.digest(input.getBytes());

            // 将字节数组转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
