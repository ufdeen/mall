package com.mall.dao;

import com.mall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByProductIdUserId(@Param("productId") Integer productId, @Param("userId")Integer userId);

    List<Cart> selectCartsByUserId(Integer userId);

    int selectCountCartByUserIdChecked(Integer userId);

    int delectCartByUserIdProducts(@Param("userId") Integer userId,@Param("products") List<String> products);

    int updateCheckByUserIdProductId(@Param("userId") Integer userId,@Param("productId") Integer productId,@Param("check") Integer check);

    int selectCountQuantityByUserId(Integer userId);
}