package com.mall.dao;

import com.mall.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    User selectLogin(@Param("username") String username,@Param("password") String password);

    int checkEmail(String email);

    String selectQuestionByUsername(String username);

    int checkAnswer(@Param("username") String username,@Param("question") String question, @Param("answer")String answer);

    int updatePasswordByUsername(@Param("username") String username, @Param("newPassword")String newPassword);

    int updatePasswordByUseridAndOldPassword(@Param("userid") Integer userid, @Param("oldPassword")String oldPassword,@Param("newPassword")String newPassword);

    int checkEmailByUserid(@Param("email") String email,@Param("userid") Integer userid);


}