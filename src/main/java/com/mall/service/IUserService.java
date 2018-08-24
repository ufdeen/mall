package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.User;

public interface IUserService {
    ServerResponse<User> login(String username,String password) ;
    ServerResponse<String> checkVaild(String str,String  type);
    ServerResponse<String> register(User user);
    ServerResponse<String> selectQuestion(String username);
    ServerResponse<String> checkAnswer(String username,String question,String answer);
    ServerResponse<String> forgetResetPassword(String username ,String newPassword,String forgetToken);
    ServerResponse<String> resetPassword(User user , String oldPassword, String newPassword);
    ServerResponse<User> updateInformation(User user);
}
