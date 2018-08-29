package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Category;
import com.mall.pojo.Product;
import com.mall.service.IProductService;
import com.mall.util.DateTimeUtil;
import com.mall.util.PropertiesUtil;
import com.mall.vo.ProductDetailVo;
import com.mall.vo.ProductListVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iProductService")
public class IProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImage = product.getSubImages().split(",");
                if (subImage.length > 0) {
                    product.setMainImage(subImage[0]);
                }
            }
            if (product.getId() == null) {
                int saveResult = productMapper.insert(product);
                if (saveResult > 0) {
                    return ServerResponse.createBySuccessMessage("新增产品成功");
                } else {
                    return ServerResponse.createByErrorMessage("新增产品失败");
                }
            } else {
                int saveResult = productMapper.updateByPrimaryKey(product);
                if (saveResult > 0) {
                    return ServerResponse.createBySuccessMessage("更新产品成功");
                } else {
                    return ServerResponse.createByErrorMessage("更新产品失败");
                }
            }
        }


        return ServerResponse.createByErrorMessage("参数不正确");
    }


    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int resultCount = productMapper.updateByPrimaryKeySelective(product);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("修改成功");
        } else {
            return ServerResponse.createByErrorMessage("修改失败");
        }
    }


    public ServerResponse<ProductDetailVo> managerProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product != null) {
            ProductDetailVo productDetailVo = assembleProductDetailVo(product);
            return ServerResponse.createBySuccess(productDetailVo);
        } else {
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
    }

    public ServerResponse<PageInfo> managerProductList(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<Product> productList = productMapper.selectProductList();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        PageInfo pageInfo = new PageInfo(productList);
        for (Product productItem : productList) {
            productListVoList.add(assembleProductListVo(productItem));
        }

        pageInfo.setList(productListVoList);
        return  ServerResponse.createBySuccess(pageInfo);
    }


    public ServerResponse<PageInfo> productSearch(String productName,Integer productId, Integer pageNo,Integer pageSize){
       if(StringUtils.isNotBlank(productName)){
           productName = new StringBuffer("%").append(productName).append("%").toString();
       }
        PageHelper.startPage(pageNo, pageSize);
        List<Product> productList = productMapper.selcetProductByNameAndId(productName,productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        PageInfo pageInfo = new PageInfo(productList);
        for (Product productItem : productList) {
            productListVoList.add(assembleProductListVo(productItem));
        }

        pageInfo.setList(productListVoList);
        return  ServerResponse.createBySuccess(pageInfo);


    }















    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return  productListVo;
    }

    /***
     * 将product对象包装成ProductDetailVo
     * */
    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setName(product.getName());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        return productDetailVo;
    }


}
