package com.mall.common;

import com.mall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Description:
 * User: uf.deen
 * Date: 2018-10-31
 */
@Component
@Slf4j
public class RedissonManager {

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port = PropertiesUtil.getIntegerProperty("redis1.port","6380");
    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port = PropertiesUtil.getIntegerProperty("redis2.port","6379");


    private Config config = new Config();
    private Redisson redisson = null;

    //使用此注解可以在类构造完成后调用
    @PostConstruct
    private void init(){
        try {
            config.useSingleServer().setAddress(new StringBuffer(redis1Ip).append(redis1Ip).toString());
            redisson = (Redisson) Redisson.create(config);
            log.info("redisson 初始化完成");
        } catch (Exception e) {
            log.info("redisson 初始化失败");
            e.printStackTrace();
        }
    }


    public Redisson getRedisson() {
        return redisson;
    }
}
