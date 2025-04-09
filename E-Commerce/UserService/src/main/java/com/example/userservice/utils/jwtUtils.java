package com.example.userservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * @Author: LH
 * @Date: 2025/3/14 16:14
 */
public class jwtUtils {

        // 独创数字签名
        private static final String signKey = "hello";

        // 令牌过期时间
        private static final Long expire = 43200000L;

        /**
         * 生成的JWT令牌
         * @param claims JWT令牌中的数据，键值对
         * @return JWT令牌
         */
        public static String generateJWT(Map<String, Object> claims) {
            return Jwts.builder()
                    .signWith(SignatureAlgorithm.HS256, signKey)
                    .addClaims(claims)
                    .setExpiration(new Date(System.currentTimeMillis() + expire))
                    .compact();
        }
        /**
         * 解析JWT令牌
         * @param jwt JWT令牌
         * @return Claims，JWT令牌中的数据
         */
        public static Claims parseJWT(String jwt) {
            return Jwts.parser()
                    .setSigningKey(signKey)
                    .parseClaimsJws(jwt)
                    .getBody();
        }

}