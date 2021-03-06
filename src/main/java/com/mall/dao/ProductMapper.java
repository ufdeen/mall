package com.mall.dao;

import com.mall.pojo.Category;
import com.mall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectProductList();

    List<Product> selcetProductByNameAndId(@Param("productName") String productName, @Param("productId") Integer productId);


    List<Product> selcetProductByNameCategory(@Param("productName") String productName, @Param("categorylist") List<Category> categorylist);

}