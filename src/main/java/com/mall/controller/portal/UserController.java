package com.mall.controller.portal;

import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.ShardedRedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            CookieUtil.writeLoginToken(httpServletResponse, session.getId());
            ShardedRedisUtil.setex(session.getId(), JsonUtil.objToJson(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXPIRETIME);
        }
        return response;
    }

    @RequestMapping(value = "check_vaild.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkVaild(String str, String type) {
        return iUserService.checkVaild(str, type);
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout( HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        CookieUtil.delLoginToken(httpServletRequest, httpServletResponse);
        ShardedRedisUtil.del(CookieUtil.readLoginToken(httpServletRequest));
        return ServerResponse.createBySuccessMessage("退出登录成功");
    }


    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session, HttpServletRequest httpServletRequest) {
        User user = this.getUserInfoByLoginInfo(httpServletRequest);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        } else {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
    }

    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        return iUserService.forgetResetPassword(username, newPassword, forgetToken);
    }

    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword (String oldPassword, String newPassword , HttpServletRequest httpServletRequest) {
        User user = this.getUserInfoByLoginInfo(httpServletRequest);
        if (user == null) {
            return ServerResponse.createByErrorMessage("当前未登录");
        }
        return iUserService.resetPassword(user, oldPassword, newPassword);
    }

    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session, User user, HttpServletRequest httpServletRequest) {
        User currentUser = this.getUserInfoByLoginInfo(httpServletRequest);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("当前未登录");
        }
        user.setId(currentUser.getId());
        ServerResponse<User> serverResponse = iUserService.updateInformation(user);
        if (serverResponse.isSuccess()) {
            currentUser.setPhone(user.getPhone());
            currentUser.setEmail(user.getEmail());
            session.setAttribute(Const.CURRENT_USER, currentUser);
        }
        serverResponse.setData(currentUser);
        return serverResponse;
    }

    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation( HttpServletRequest httpServletRequest) {
        User currentUser = this.getUserInfoByLoginInfo(httpServletRequest);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "当前未登录,进行强制登录");
        }
        return ServerResponse.createBySuccess(currentUser);
    }


    /**
     * 从redis获取当前用户登陆信息
     * @param request
     * @return
     */
    public User getUserInfoByLoginInfo(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return null;
        }
        User user = JsonUtil.jsonToObj(ShardedRedisUtil.get(loginToken), User.class);
        return  user;
    }


}
