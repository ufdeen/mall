package com.mall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.Shipping;
import com.mall.pojo.User;
import com.mall.service.IShippinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippinService iShippinService;

    @RequestMapping("add_shipping.do")
    public ServerResponse<Map> addShipping(Shipping shipping, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("当前未登录");
        }

        return iShippinService.addShipping(shipping, user.getId());

    }

    @RequestMapping("update_shipping.do")
    public ServerResponse<Map> updateShipping(Shipping shipping, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("当前未登录");
        }

        return iShippinService.updateShipping(shipping, user.getId());
    }

    @RequestMapping("delete_shipping.do")
    public ServerResponse<Map> delectShipping(Integer shippingId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("当前未登录");
        }

        return iShippinService.delectShipping(shippingId, user.getId());
    }

    @RequestMapping("select_shipping.do")
    public ServerResponse<Shipping> selectShipping(Integer shippingId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("当前未登录");
        }

        return iShippinService.selectShipping(shippingId, user.getId());
    }

    @RequestMapping("list_shipping.do")
    public ServerResponse<PageInfo> listShipping(HttpSession session, @RequestParam(value = "pageNo",defaultValue = "0") Integer pageNo,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("当前未登录");
        }

        return  iShippinService.listShipping(user.getId(),pageNo,pageSize);
    }


}
