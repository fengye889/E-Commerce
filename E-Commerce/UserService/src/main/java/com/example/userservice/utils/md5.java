package com.example.userservice.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: LH
 * @Date: 2025/4/2 16:50
 */
public class md5 {
    public static String md5Encrypt(String input) {
        try {
            // 获取 MD5 算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 对输入的字符串进行哈希处理
            byte[] messageDigest = md.digest(input.getBytes());

            // 转换为十六进制表示
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b)); // 转换为 2 位的十六进制
            }

            return hexString.toString();  // 返回加密后的密码（MD5）
        } catch (NoSuchAlgorithmException e) {
            // 若没有找到 MD5 算法，抛出异常
            System.err.println("MD5 算法未找到！");
            return null;
        }
    }
}