package com.mall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)  //序列化时排除值为null的key
public class ServerResponse<T> {

    private int status;
    private String msg;
    private T data;


    private ServerResponse(int status){
        this.status = status;
    }
    private ServerResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status,T data){
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status,String msg,T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore //设置json序列号时排除此字段
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ServerResponse<T> createBySuccess(){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerResponse<T> createByError(){
        return  new ServerResponse<T>(ResponseCode.ERROR.getCode());
    }



    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return  new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }
    public static <T> ServerResponse<T> createByErrorCodeMessage(int errCode,String errorMessage){
        return  new ServerResponse<T>(errCode,errorMessage);
    }



}
