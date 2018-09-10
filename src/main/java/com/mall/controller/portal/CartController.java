package com.mall.controller.portal;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.ICartService;
import com.mall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer productId, Integer count){
        User user  =  (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return  ServerResponse.createByErrorMessage("当前未登录");
        }
        return iCartService.addCart(user.getId(), productId, count);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session, Integer productId, Integer count){
        User user  =  (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return  ServerResponse.createByErrorMessage("当前未登录");
        }
        return iCartService.updateCart(user.getId(), productId, count);
    }

    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> delete(HttpSession session, String productIds){
        User user  =  (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return  ServerResponse.createByErrorMessage("当前未登录");
        }
        return iCartService.deleteProduct(user.getId(), productIds);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session){
        User user  =  (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return  ServerResponse.createByErrorMessage("当前未登录");
        }
        return iCartService.list(user.getId());
    }

    @RequestMapping("check_all.do")
    @ResponseBody
    public ServerResponse<CartVo> checkAll(HttpSession session){
        User user  =  (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return  ServerResponse.createByErrorMessage("当前未登录");
        }
        return iCartService.selectOrUnselect(user.getId(),null,Const.Cart.CHECKED);
    }

    @RequestMapping("uncheck_all.do")
    @ResponseBody
    public ServerResponse<CartVo> uncheckAll(HttpSession session){
        User user  =  (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return  ServerResponse.createByErrorMessage("当前未登录");
        }
        return iCartService.selectOrUnselect(user.getId(),null,Const.Cart.UN_CHECKED);
    }

    @RequestMapping("check_one.do")
    @ResponseBody
    public ServerResponse<CartVo> checkOne(HttpSession session,Integer productId){
        User user  =  (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return  ServerResponse.createByErrorMessage("当前未登录");
        }
        return iCartService.selectOrUnselect(user.getId(),productId,Const.Cart.CHECKED);
    }

    @RequestMapping("uncheck_one.do")
    @ResponseBody
    public ServerResponse<CartVo> uncheckOne(HttpSession session,Integer productId){
        User user  =  (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return  ServerResponse.createByErrorMessage("当前未登录");
        }
        return iCartService.selectOrUnselect(user.getId(),productId,Const.Cart.UN_CHECKED);
    }

    @RequestMapping("count_quantity.do")
    @ResponseBody
    public ServerResponse<Integer> countQuantity(HttpSession session,Integer productId){
        User user  =  (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return  ServerResponse.createBySuccess(0);
        }
        return iCartService.countQuantity(user.getId());
    }

}
