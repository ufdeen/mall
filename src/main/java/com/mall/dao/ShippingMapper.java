package com.mall.dao;

import com.mall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int updateByPrimaryKeyUserId(Shipping record);

    int deleteByPrimaryKeyUserId(@Param("id") Integer id, @Param("userId") Integer userId);

    Shipping selectByPrimaryKeyUserId(@Param("id") Integer id, @Param("userId") Integer userId);

    List<Shipping> listByUserId(Integer userId);
}