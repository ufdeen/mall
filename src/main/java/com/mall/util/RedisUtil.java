package com.mall.util;

import com.mall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

@Slf4j
public class RedisUtil {
    /**
     * 设置键的超时时间
     * @param key 键
     * @param exTime 超时时间 单位秒
     * @return
     */
    public static Long expire(String key,int exTime){
        Jedis jedis = null;
        Long result = null;
        try{
            jedis = RedisPool.getResource();
            result = jedis.expire(key,exTime);
        }catch (Exception e){
            log.error(" expire key:{} error",key,e);
        }finally {
            RedisPool.returnResource(jedis);
        }
        return  result;
    }

    /**
     * 设置键值并加入超时时间
     * @param key 键
     * @param value 值
     * @param exTime 超时时间 单位秒
     * @return
     */
    public static String setex(String key,String value,int exTime){
        Jedis jedis = null;
        String result = null;
        try{
            jedis = RedisPool.getResource();
            result = jedis.setex(key,exTime,value);
        }catch (Exception e){
            log.error(" setex key:{} value:{} error",key,value,e);
        }finally {
            RedisPool.returnResource(jedis);
        }
        return  result;
    }

    public static String set(String key,String value){
        Jedis jedis = null;
        String result = null;
        try{
            jedis = RedisPool.getResource();
            result = jedis.set(key,value);
        }catch (Exception e){
            log.error(" set key:{} value:{} error",key,value,e);
        }finally {
            RedisPool.returnResource(jedis);
        }
        return  result;
    }

    public static String get(String key){
        Jedis jedis = null;
        String result = null;
        try{
            jedis = RedisPool.getResource();
            result = jedis.get(key);
        }catch (Exception e){
            log.error(" get key:{} error",key,e);
        }finally {
            RedisPool.returnResource(jedis);
        }
        return  result;
    }


    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;
        try{
            jedis = RedisPool.getResource();
            result = jedis.del(key);
        }catch (Exception e){
            log.error(" del key:{} error",key,e);
        }finally {
            RedisPool.returnResource(jedis);
        }
        return  result;
    }

}
