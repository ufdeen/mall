package com.mall.util;

import com.mall.common.ShardedRedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

@Slf4j
public class ShardedRedisUtil {
    /**
     * 设置键的超时时间
     * @param key 键
     * @param exTime 超时时间 单位秒
     * @return
     */
    public static Long expire(String key,int exTime){
        ShardedJedis jedis = null;
        Long result = null;
        try{
            jedis = ShardedRedisPool.getResource();
            result = jedis.expire(key,exTime);
        }catch (Exception e){
            log.error(" expire key:{} error",key,e);
        }finally {
            ShardedRedisPool.returnResource(jedis);
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
        ShardedJedis jedis = null;
        String result = null;
        try{
            jedis = ShardedRedisPool.getResource();
            result = jedis.setex(key,exTime,value);
        }catch (Exception e){
            log.error(" setex key:{} value:{} error",key,value,e);
        }finally {
            ShardedRedisPool.returnResource(jedis);
        }
        return  result;
    }

    public static String set(String key,String value){
        ShardedJedis jedis = null;
        String result = null;
        try{
            jedis = ShardedRedisPool.getResource();
            result = jedis.set(key,value);
        }catch (Exception e){
            log.error(" set key:{} value:{} error",key,value,e);
        }finally {
            ShardedRedisPool.returnResource(jedis);
        }
        return  result;
    }

    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;
        try{
            jedis = ShardedRedisPool.getResource();
            result = jedis.get(key);
        }catch (Exception e){
            log.error(" get key:{} error",key,e);
        }finally {
            ShardedRedisPool.returnResource(jedis);
        }
        return  result;
    }


    public static Long del(String key){
        ShardedJedis jedis = null;
        Long result = null;
        try{
            jedis = ShardedRedisPool.getResource();
            result = jedis.del(key);
        }catch (Exception e){
            log.error(" del key:{} error",key,e);
        }finally {
            ShardedRedisPool.returnResource(jedis);
        }
        return  result;
    }


    public static void main(String[] args) {
        for(int i = 0; i<20; i++){
            ShardedRedisUtil.set(i+"",i+"");
        }
    }

}
