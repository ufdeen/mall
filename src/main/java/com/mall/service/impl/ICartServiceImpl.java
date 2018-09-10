package com.mall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.dao.CartMapper;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Cart;
import com.mall.pojo.Product;
import com.mall.service.ICartService;
import com.mall.util.BigDecimalUtil;
import com.mall.util.PropertiesUtil;
import com.mall.vo.CartProductVo;
import com.mall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("iCartService")
public class ICartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    public ServerResponse<CartVo> addCart(Integer userId,Integer productId,Integer count){
        if(productId == null && count == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByProductIdUserId(productId,userId);
        if(cart != null){
            //更新数量
            cart.setQuantity(cart.getQuantity()+count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }else{
            //新加入购物车
            Cart newCart = new Cart();
            newCart.setProductId(productId);
            newCart.setChecked(Const.Cart.CHECKED);
            newCart.setQuantity(count);
            newCart.setUserId(userId);
            cartMapper.insert(newCart);
        }

       return  this.list(userId);

    }

    public ServerResponse<CartVo> updateCart(Integer userId,Integer productId,Integer count){
        if(productId == null && count == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByProductIdUserId(productId,userId);
        if(cart != null){
            //更新数量
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }

        return  this.list(userId);

    }


    public ServerResponse<CartVo> deleteProduct(Integer userId,String productIds){
        if(StringUtils.isBlank(productIds)){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productIdList)){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.delectCartByUserIdProducts(userId, productIdList);
        return  this.list(userId);

    }

    public ServerResponse<CartVo> list(Integer userId){
        CartVo cartVo = getCartVoLimit(userId);
        return  ServerResponse.createBySuccess(cartVo);
    }



    public ServerResponse<CartVo> selectOrUnselect(Integer userId,Integer productId,Integer check){
        cartMapper.updateCheckByUserIdProductId(userId,productId,check);
        return list(userId);
    }


    public ServerResponse<Integer> countQuantity(Integer userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }

        return  ServerResponse.createBySuccess(cartMapper.selectCountQuantityByUserId(userId));
    }


    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        List<Cart> carts = cartMapper.selectCartsByUserId(userId);
        if(CollectionUtils.isNotEmpty(carts)){
            for(Cart cartItem : carts){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product!=null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    int buyLimitCount = 0;
                    if(product.getStock() >= cartItem.getQuantity()){
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //库存不够把购物车商品数量加到库存最大数
                        Cart cartLimitQuantity = new Cart();
                        cartLimitQuantity.setQuantity(buyLimitCount);
                        cartLimitQuantity.setId(cartItem.getId());
                        cartMapper.updateByPrimaryKeySelective(cartLimitQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算单个商品的总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),buyLimitCount));
                    cartProductVo.setProductChecked(cartItem.getChecked());

                    if(Const.Cart.CHECKED == cartItem.getChecked()){
                        //当前购物车此商品选中，加入总价格中
                        cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                    }
                    cartProductVoList.add(cartProductVo);
                }
            }

        }
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        return  cartVo;
    }


    /***
     * 用户购物车物品是否全部选中
     * */
    private boolean getAllCheckedStatus(Integer userId){
        if(userId == null){
            return false;
        }
       return cartMapper.selectCountCartByUserIdChecked(userId) == 0 ;


    }

}
