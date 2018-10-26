package com.mall.common;

import com.mall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
    private static JedisPool pool; //jedis连接池
    private static Integer maxTotal = PropertiesUtil.getIntegerProperty("redis.maxTotal","100");
    private static Integer maxIdle= PropertiesUtil.getIntegerProperty("redis.maxIdle","20");;
    private static Integer minIdle= PropertiesUtil.getIntegerProperty("redis.minIdle","5");;
    private static boolean testOnBorrow= PropertiesUtil.getBooleanProperty("redis.testOnBorrow","true");;
    private static boolean testOnReturn= PropertiesUtil.getBooleanProperty("redis.testOnReturn","true");

    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    private static Integer redisPort = PropertiesUtil.getIntegerProperty("redis.port","6379");


    private static void initRedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setMinIdle(minIdle);

        //config.setTestOnBorrow(testOnBorrow);
        // config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true); //连接耗尽时，是否阻塞  false抛出异常，true阻塞直到超时

        pool = new JedisPool(config,redisIp,redisPort,1000*2);
    }

    static {
        initRedisPool();
    }


    public static Jedis getResource(){
        return  pool.getResource();
    }

    public static  void returnResource(Jedis jedis){
       jedis.close();
    }


}
