package com.mall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN = ".ufdeen.com";
    private final static String COOKIE_NAME = "mall_login_token";

    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);  //设置cookie作用在.ufdeen.com顶级域名
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7);  //设置cookie有效期7天
        cookie.setHttpOnly(true);  //设置禁止javascript脚本获取cookie，防止攻击
        log.info("writeLoginToken token:{}", token);
        response.addCookie(cookie);
    }

    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    log.info("readLoginToken token:{}" ,cookie.getValue());
                    return cookie.getValue();
                }
            }
        }

        return  null;
    }

    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    log.info("delLoginToken token:{}" ,cookie.getValue());
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    cookie.setMaxAge(0); //设置0代表删除此cookie
                    response.addCookie(cookie);
                }
            }
        }

    }

}
