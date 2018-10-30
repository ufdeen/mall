package com.mall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public static final String TOKEN_PREFIX = "token_"; //生成token的前缀

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;

    }

    public interface ProductListOrderBy{
        Set<String> PRICE_DESC_ASC = Sets.newHashSet("price_desc","price_asc");
    }

    public interface Cart{
        int CHECKED = 1;
        int UN_CHECKED = 0;
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface AlipayCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS  = "success";
        String RESPONSE_FAILED  = "failed";
    }

    public enum PaymentTypeEnum{
        ONLINE_PAY(1,"在线支付");

        PaymentTypeEnum(int code,String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static PaymentTypeEnum codeOf(int code){
            for(PaymentTypeEnum paymentTypeEnum : values()){
                if(paymentTypeEnum.getCode() == code){
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("么有找到对应的枚举");
        }

    }

    /**
     * 登陆用户设置参数
     * */
    public interface RedisCacheExtime{
        int REDIS_SESSION_EXPIRETIME = 60*30;
    }


    /***
     * redis 分布式锁
     * */
    public interface RedisLock{
        String CLOSE_ORDER_LOCK = "CLOSE_ORDER_LOCK";
    }
}
