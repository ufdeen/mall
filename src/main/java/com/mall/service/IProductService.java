package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.pojo.Product;
import com.mall.vo.ProductDetailVo;

public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);
    ServerResponse setSaleStatus(Integer productId,Integer status);
    ServerResponse<ProductDetailVo> managerProductDetail(Integer productId );
    ServerResponse<PageInfo> managerProductList(Integer pageNo, Integer pageSize);
    ServerResponse<PageInfo> productSearch(String productName,Integer productId, Integer pageNo,Integer pageSize);
}
