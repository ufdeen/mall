package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.mall.common.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.pojo.Category;
import com.mall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("iCategoryService")
public class ICategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(ICategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setStatus(true);
        category.setParentId(parentId);
        int countResult = categoryMapper.insert(category);
        if(countResult>0){
            return ServerResponse.createBySuccessMessage("添加成功");
        }else{
            return ServerResponse.createByErrorMessage("添加失败");
        }

    }

    public ServerResponse<String> updateCategoryName(String categoryName,Integer categoryId){
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);

        int countResult = categoryMapper.updateByPrimaryKeySelective(category);
        if(countResult>0){
            return ServerResponse.createBySuccessMessage("修改成功");
        }else{
            return ServerResponse.createByErrorMessage("修改失败");
        }

    }

    public ServerResponse<List<Category>> getCategoryChildrenByCategoryId(Integer categoryId){
        if (categoryId == null ) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        List<Category> categoryList = categoryMapper.getCategoryChildrenByCategoryId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类--->categoryId="+categoryId);
        }
        return  ServerResponse.createBySuccess(categoryList);
    }

    public ServerResponse<List<Category>> getCategoryDeepChildrenByCategoryId(Integer categoryId){
        if (categoryId == null ) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        ArrayList<Category> categoryList = Lists.newArrayList();
        findChildCategory(categoryList,categoryId);
        return  ServerResponse.createBySuccess(categoryList);
    }

    private void findChildCategory(ArrayList<Category> categoryList,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categoryList.add(category);
        }
        List<Category> categoryChildList = categoryMapper.getCategoryChildrenByCategoryId(categoryId);
        for(Category categoryItem : categoryChildList){
            findChildCategory(categoryList,categoryItem.getId());
        }
        return;
    }


}
