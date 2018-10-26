package com.mall.controller.common;

import com.mall.common.Const;
import com.mall.pojo.User;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.ShardedRedisUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 访问时将用户登陆过期时间重新设置
 *
 * */
public class SessionExpirFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)) {
            User user = JsonUtil.jsonToObj(ShardedRedisUtil.get(loginToken), User.class);
            if(user != null){
                ShardedRedisUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXPIRETIME);
            }
        }

        filterChain.doFilter(servletRequest,servletResponse);

    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }



    @Override
    public void destroy() {

    }
}
