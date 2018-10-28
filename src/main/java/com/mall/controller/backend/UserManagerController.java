package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.ShardedRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager/user/")
public class UserManagerController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping(value="login.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User>  login(String username, String password, HttpSession session,HttpServletResponse httpServletResponse) {
        ServerResponse<User> response = iUserService.login(username, password);
        if(response.isSuccess()){
            User user = response.getData();
            if(Const.Role.ROLE_ADMIN == user.getRole()){
                CookieUtil.writeLoginToken(httpServletResponse, session.getId());
                ShardedRedisUtil.setex(session.getId(), JsonUtil.objToJson(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXPIRETIME);
            }else{
                return  ServerResponse.createByErrorMessage("非管理员用户，无法登录");
            }
        }
        return  response;
    }


}
