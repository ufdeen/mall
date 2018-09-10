package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.vo.CartVo;

public interface ICartService {
    ServerResponse<CartVo> addCart(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVo> updateCart(Integer userId,Integer productId,Integer count);
    ServerResponse<CartVo> deleteProduct(Integer userId,String productIds);
    ServerResponse<CartVo> list(Integer userId);
    ServerResponse<CartVo> selectOrUnselect(Integer userId,Integer productId,Integer check);
    ServerResponse<Integer> countQuantity(Integer userId);
}
