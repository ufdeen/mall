package com.mall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.ShardedRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Description:
 * User: uf.deen
 * Date: 2018-10-28
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) o;
        String className = handlerMethod.getBean().getClass().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();

        //登陆不使用拦截器拦截
        if (StringUtils.equals(className, "UserManagerController") && StringUtils.equals(methodName, "login")) {
            return true;
        }

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        User user = JsonUtil.jsonToObj(ShardedRedisUtil.get(loginToken), User.class);
        if (user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)) {
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = httpServletResponse.getWriter();
            if (user == null) {
                //富文本上传的返回值不同，需特殊处理
                if (StringUtils.equals(className, "ProductMangerController") && StringUtils.equals(methodName, "richtextImgUpload")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "当前未登录");
                    writer.write(JsonUtil.objToJson(resultMap));
                } else {
                    writer.write(JsonUtil.objToJson(ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "当前未登录,进行强制登录")));
                }
            } else {
                if (StringUtils.equals(className, "ProductMangerController") && StringUtils.equals(methodName, "richtextImgUpload")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "当前非管理员用户");
                    writer.write(JsonUtil.objToJson(resultMap));
                } else {
                    writer.write(JsonUtil.objToJson(ServerResponse.createByErrorMessage("非管理员用户，无法登录")));
                }
            }
            writer.flush();
            writer.close();
            return false;
        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
