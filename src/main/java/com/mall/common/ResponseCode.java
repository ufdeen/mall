package com.mall.common;

public enum  ResponseCode {
    SUCCESS(0,"success"),
    ERROR(1,"error"),
    NEED_LOGIN(10,"need_login"),
    ILLEGAL_ARGUMENT(2,"illegal_argument");

    private int code;
    private String desc;

    private ResponseCode(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
