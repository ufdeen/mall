package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mall.common.ServerResponse;
import com.mall.dao.ShippingMapper;
import com.mall.pojo.Shipping;
import com.mall.service.IShippinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("iShippinService")
public class IShippinServiceImpl implements IShippinService{

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse<Map> addShipping(Shipping shipping,Integer userId){
        shipping.setUserId(userId);
        int countResult = shippingMapper.insert(shipping);
        if(countResult > 0){
            Map map = Maps.newHashMap();
            map.put("shippingId",shipping.getId());
            return  ServerResponse.createBySuccess(map);
        }else{
            return  ServerResponse.createByErrorMessage("新增地址失败");
        }
    }

    public ServerResponse<Map> updateShipping(Shipping shipping,Integer userId){
        shipping.setUserId(userId);
        int countResult = shippingMapper.updateByPrimaryKeyUserId(shipping);
        if(countResult > 0){
            return  ServerResponse.createBySuccessMessage("更新地址成功");
        }else{
            return  ServerResponse.createByErrorMessage("更新地址失败");
        }
    }

    public ServerResponse<Map> delectShipping(Integer shippingId,Integer userId){
        int countResult = shippingMapper.deleteByPrimaryKeyUserId(shippingId,userId);
        if(countResult > 0){
            return  ServerResponse.createBySuccessMessage("删除地址成功");
        }else{
            return  ServerResponse.createByErrorMessage("删除地址失败");
        }
    }

    public ServerResponse<Shipping> selectShipping(Integer shippingId,Integer userId){
        Shipping shipping = shippingMapper.selectByPrimaryKeyUserId(shippingId,userId);
        return  ServerResponse.createBySuccess(shipping);
    }

    public ServerResponse<PageInfo> listShipping(Integer userId, Integer pageNo, Integer pageSize){
        PageHelper.startPage(pageNo,pageSize);
        List<Shipping> shippings = shippingMapper.listByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippings);
        return  ServerResponse.createBySuccess(pageInfo);

    }


}
