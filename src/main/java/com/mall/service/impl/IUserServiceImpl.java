package com.mall.service.impl;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.common.TokenCache;
import com.mall.dao.UserMapper;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class IUserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        //判断用户是否存在
        if(userMapper.checkUsername(username) < 1 ){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        //登录
        String md5Password = MD5Util.MD5Encode(password);
        User user = userMapper.selectLogin(username,md5Password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }


        user.setPassword("");
        return ServerResponse.createBySuccess("登录成功",user);
    }

    @Override
    public ServerResponse<String> checkVaild(String str, String type) {
        if(StringUtils.isNotBlank(type)){
            if(Const.USERNAME.equals(type)){
                if(userMapper.checkUsername(str) >= 1 ){
                    return ServerResponse.createByErrorMessage("用户已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                if(userMapper.checkEmail(str) >= 1 ){
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return  ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> vaildEmail = checkVaild(user.getEmail(), Const.EMAIL);
        if(!vaildEmail.isSuccess()){
            return vaildEmail;
        }
        ServerResponse<String> vaildUsername = checkVaild(user.getUsername(), Const.USERNAME);
        if(!vaildUsername.isSuccess()){
            return vaildUsername;
        }

        //设置权限
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //密码处理
        user.setPassword(MD5Util.MD5Encode(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if(resultCount==0){
            return  ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> selectQuestion(String username){
        ServerResponse<String> vaildResponse = this.checkVaild(username, Const.USERNAME);
        if(vaildResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }else{
            String question = userMapper.selectQuestionByUsername(username);
            if(StringUtils.isNotBlank(question)){
                return  ServerResponse.createBySuccess(question);
            }else{
                return  ServerResponse.createByErrorMessage("找回密码问题为空");
            }
        }
    }

    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int countResult = userMapper.checkAnswer(username, question, answer);
        if(countResult > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return  ServerResponse.createBySuccess(forgetToken);
        }else{
            return  ServerResponse.createByErrorMessage("问题的答案错误");
        }
    }

    public ServerResponse<String> forgetResetPassword(String username ,String newPassword,String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return  ServerResponse.createByErrorMessage("参数错误，forgetToken需要传递");
        }
        ServerResponse<String> vaildResponse = this.checkVaild(username, Const.USERNAME);
        if(vaildResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.equals(token,forgetToken)){
            String md5password = MD5Util.MD5Encode(newPassword);
            int countResult = userMapper.updatePasswordByUsername(username, md5password);
            if(countResult>0){
                return  ServerResponse.createBySuccessMessage("密码修改成功");
            }else{
                return  ServerResponse.createByErrorMessage("密码修改失败");
            }
        }else{
            return ServerResponse.createByErrorMessage("token值错误");
        }

    }


    public ServerResponse<String> resetPassword(User user , String oldPassword, String newPassword){
        int countResult = userMapper.updatePasswordByUseridAndOldPassword(user.getId(), MD5Util.MD5Encode(oldPassword), MD5Util.MD5Encode(newPassword));
        if(countResult>0){
            return  ServerResponse.createBySuccessMessage("密码修改成功");
        }else{
            return  ServerResponse.createByErrorMessage("密码修改失败");
        }
    }

    public ServerResponse<User> updateInformation(User user){
        User updateUser = new User();
        int countResult = userMapper.checkEmailByUserid(user.getEmail(), user.getId());
        if(countResult>0){
            return ServerResponse.createByErrorMessage("修改的email已被使用");
        }

        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        int updateCountResult = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCountResult>0){
            return ServerResponse.createBySuccess("修改成功",updateUser);
        }else{
            return ServerResponse.createByErrorMessage("修改失败");
        }
    }

    public ServerResponse checkAdminRole(User user){
        if(user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

}
