package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.Category;
import com.mall.pojo.User;
import com.mall.service.ICategoryService;
import com.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/manager/category/")
public class CategoryMangerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value="add_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") Integer parentId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前未登录,进行强制登录");
        }
        if(!iUserService.checkAdminRole(currentUser).isSuccess()){
            return  ServerResponse.createByErrorMessage("非管理员用户，无法登录");
        }
        return  iCategoryService.addCategory(categoryName,parentId);
    }

    @RequestMapping(value="set_category_name.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> setCategoryName(HttpSession session,String categoryName,Integer categoryId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前未登录,进行强制登录");
        }
        if(!iUserService.checkAdminRole(currentUser).isSuccess()){
            return  ServerResponse.createByErrorMessage("非管理员用户，无法登录");
        }
        return  iCategoryService.updateCategoryName(categoryName,categoryId);
    }

    @RequestMapping(value="get_category_children.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Category>> getCategoryChildrenByCategoryId(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前未登录,进行强制登录");
        }
        if(!iUserService.checkAdminRole(currentUser).isSuccess()){
            return  ServerResponse.createByErrorMessage("非管理员用户，无法登录");
        }
       return iCategoryService.getCategoryChildrenByCategoryId(categoryId);
    }


    @RequestMapping(value="get_category_deepchildren.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Category>> getCategoryDeepChildrenByCategoryId(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前未登录,进行强制登录");
        }
        if(!iUserService.checkAdminRole(currentUser).isSuccess()){
            return  ServerResponse.createByErrorMessage("非管理员用户，无法登录");
        }
        return  iCategoryService.getCategoryDeepChildrenByCategoryId(categoryId);
    }

}
