package com.mall.common;

public enum ProductStatusEnum {
    ON_SALE(1,"on_sale");

    private int code;
    private String value;

    ProductStatusEnum(int code,String value){
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
