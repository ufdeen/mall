package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.pojo.Shipping;

import java.util.Map;

public interface IShippinService {
    ServerResponse<Map> addShipping(Shipping shipping, Integer userId);
    ServerResponse<Map> updateShipping(Shipping shipping,Integer userId);
    ServerResponse<Map> delectShipping(Integer shippingId,Integer userId);
    ServerResponse<Shipping> selectShipping(Integer shippingId,Integer userId);
    ServerResponse<PageInfo> listShipping(Integer userId, Integer pageNo, Integer pageSize);
}
