package com.mall.common;

import com.google.common.collect.Lists;
import com.mall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.List;

public class ShardedRedisPool {
    private static ShardedJedisPool pool; //jedis连接池
    private static Integer maxTotal = PropertiesUtil.getIntegerProperty("redis.maxTotal","100");
    private static Integer maxIdle= PropertiesUtil.getIntegerProperty("redis.maxIdle","20");;
    private static Integer minIdle= PropertiesUtil.getIntegerProperty("redis.minIdle","5");;
    private static boolean testOnBorrow= PropertiesUtil.getBooleanProperty("redis.testOnBorrow","true");;
    private static boolean testOnReturn= PropertiesUtil.getBooleanProperty("redis.testOnReturn","true");

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port = PropertiesUtil.getIntegerProperty("redis1.port","6380");
    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port = PropertiesUtil.getIntegerProperty("redis2.port","6379");


    private static void initRedisShardPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setMinIdle(minIdle);

        //config.setTestOnBorrow(testOnBorrow);
        // config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true); //连接耗尽时，是否阻塞  false抛出异常，true阻塞直到超时
        JedisShardInfo info1 = new JedisShardInfo(redis1Ip,redis1Port,2*1000);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip,redis2Port,2*1000);
        List<JedisShardInfo> jedisShardInfoList = Lists.newArrayList(info1,info2);
        pool = new ShardedJedisPool(config,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static {
        initRedisShardPool();
    }


    public static ShardedJedis getResource(){
        return  pool.getResource();
    }

    public static  void returnResource(ShardedJedis jedis){
       jedis.close();
    }


}
