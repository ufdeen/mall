package com.mall.dao;

import com.mall.pojo.Category;
import com.mall.pojo.Product;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> getCategoryChildrenByCategoryId(Integer categoryId);

}