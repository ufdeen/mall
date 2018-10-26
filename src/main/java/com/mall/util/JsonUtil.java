package com.mall.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mall.pojo.Order;
import com.mall.pojo.User;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;

@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //设置所有字段都进行序列化
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);
        //设置忽略空Bean造成的异常
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
        //设置时间的转换格式  （yyyy-MM-dd HH:mm:ss）
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.FORMAT_TIME));
        //设置反序列化时json字符串存在但java对象中不存在此属性的异常
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES,false);
    }

    /**
     * 对象转json
     * */
    public static <T> String objToJson(T o){
        if(o == null){
            return null;
        }
        try {
            return o instanceof String ? (String)o : objectMapper.writeValueAsString(o);
        }catch (Exception e){
            log.warn("parse Object to String error ",e);
            return null;
        }
    }

    /**
     * 对象转json 带格式
     * */
    public static <T> String objToJsonPretty(T o){
        if(o == null){
            return null;
        }
        try {
            return o instanceof String ? (String)o : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        }catch (Exception e){
            log.warn("parse Object to String error ",e);
            return null;
        }
    }

    /**
     * json转字符串
     * @param clazz 转换的对象的class
     * */
    public static <T> T jsonToObj(String json,Class<T> clazz){
        if(StringUtils.isEmpty(json) || clazz == null){
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T)json : objectMapper.readValue(json,clazz);
        }catch (Exception e){
            log.warn("parse String to Object error ",e);
            return null;
        }
    }

    /**
     * json转字符串
     * 使用TypeReference来规范返回的对象类型
     * @param typeReference  传递new TypeReference<Map<String,List<XXX>>>() {}) 指定泛型方式来规范返回值
     * */
    public static <T> T jsonToObj(String json, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(json) || typeReference == null){
            return null;
        }
        try {
            return typeReference.getType().equals(String.class) ? (T)json : objectMapper.readValue(json,typeReference);
        }catch (Exception e){
            log.warn("parse String to Object error ",e);
            return null;
        }
    }

    public static <T> T jsonToObj(String json, Class<?> collectionClass,Class<?>... elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try {
            return  objectMapper.readValue(json,javaType);
        }catch (Exception e){
            log.warn("parse String to Object error ",e);
            return null;
        }
    }



}
