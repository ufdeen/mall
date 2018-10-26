package com.mall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/order/")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private IOrderService orderService;

    @Autowired
    private UserController userController;

    @RequestMapping("create.do")
    @ResponseBody
    public ServerResponse create(HttpServletRequest httpServletRequest, Integer shippingId){
        User user = userController.getUserInfoByLoginInfo(httpServletRequest);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.createOrder(user.getId(),shippingId);
    }


    @RequestMapping("cancel.do")
    @ResponseBody
    public ServerResponse cancel(HttpServletRequest httpServletRequest, Long orderNo){
        User user = userController.getUserInfoByLoginInfo(httpServletRequest);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.cancel(user.getId(),orderNo);
    }


    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpServletRequest httpServletRequest){
        User user = userController.getUserInfoByLoginInfo(httpServletRequest);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderCartProduct(user.getId());
    }



    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpServletRequest httpServletRequest,Long orderNo){
        User user = userController.getUserInfoByLoginInfo(httpServletRequest);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderDetail(user.getId(),orderNo);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = userController.getUserInfoByLoginInfo(httpServletRequest);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderList(user.getId(),pageNum,pageSize);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////

    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse<Map> pay(HttpServletRequest httpServletRequest, HttpServletRequest request, Long orderNo) {
        User user = userController.getUserInfoByLoginInfo(httpServletRequest);
        if (user == null) {
            return ServerResponse.createByErrorMessage("当前未登录");
        }

        String path = request.getSession().getServletContext().getRealPath("upload");
        return orderService.pay(orderNo, user.getId(), path);

    }

    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {
            Map<String, String> params = Maps.newHashMap();
            Map<String, String[]> parameterMap = request.getParameterMap();
            //获取所有返回参数并且封装进map中
            for (Iterator iterator = parameterMap.keySet().iterator(); iterator.hasNext(); ) {
                String name = (String) iterator.next();
                String[] values = parameterMap.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }

            logger.info("付款回调参数：" + params);
            //验证参数合法性
            params.remove("sign_type");
            try {
                boolean alipayCallbackValid = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
                if (!alipayCallbackValid) {
                    //验证未通过
                    return ServerResponse.createByErrorMessage("参数错误，验证不通过");
                }
            } catch (AlipayApiException e) {
                logger.error("付款回调失败", e);
                return ServerResponse.createByErrorMessage("付款回调失败");
            }

            //验证订单合法性，修改订单状态
            ServerResponse serverResponse = orderService.alipayCallback(params);
            if (serverResponse.isSuccess()) {
                return Const.AlipayCallback.RESPONSE_SUCCESS;
            } else {
                return Const.AlipayCallback.RESPONSE_FAILED;
            }
    }

    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpServletRequest httpServletRequest,Long orderNo){
        User user = userController.getUserInfoByLoginInfo(httpServletRequest);
        if (user == null) {
            return ServerResponse.createByErrorMessage("当前未登录");
        }
        if(orderService.queryOrderPayStatus(user.getId(),orderNo).isSuccess()){
            return  ServerResponse.createBySuccess(true);
        }else{
            return  ServerResponse.createBySuccess(false);
        }

    }



}
