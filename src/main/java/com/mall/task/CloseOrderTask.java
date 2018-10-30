package com.mall.task;

import com.mall.common.Const;
import com.mall.service.IOrderService;
import com.mall.util.PropertiesUtil;
import com.mall.util.ShardedRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

/**
 * Description: 取消过期订单定时任务
 * User: uf.deen
 * Date: 2018-10-29
 */
@Component
@Slf4j
public class CloseOrderTask {
    @Autowired
    private IOrderService iOrderService;

    //@Scheduled(cron="0 0/1 * * * ?")
    public void closeTaskV1(){
        log.info("关闭订单任务开始执行");
        iOrderService.closeOrder(PropertiesUtil.getIntegerProperty("task.closeOrder","2"));

    }

    @Scheduled(cron="0 0/1 * * * ?")
    /**
     * 使用redis锁来防止死锁
     * */
    public void closeTaskV2(){
        log.info("关闭订单任务始执行");
        Long value = System.currentTimeMillis()+Long.parseLong(PropertiesUtil.getProperty("redis.locktime"));
        Long result = ShardedRedisUtil.setnx(Const.RedisLock.CLOSE_ORDER_LOCK, value.toString());
        if(result != null && result.intValue() == 1){
            //获取锁成功，可以进行业务操作
            closeOrder(Const.RedisLock.CLOSE_ORDER_LOCK);
        }else{
            //获取锁失败，查看锁是否超时
            String lockValue = ShardedRedisUtil.get(Const.RedisLock.CLOSE_ORDER_LOCK);
            if(lockValue != null || (lockValue!=null && System.currentTimeMillis() > Long.parseLong(lockValue))){
                //已经超时，有权利获取锁,但是还是要获取现在的值判断两次获取的值是否相等
                value = System.currentTimeMillis()+Long.parseLong(PropertiesUtil.getProperty("redis.locktime"));
                String getSetValue = ShardedRedisUtil.getSet(Const.RedisLock.CLOSE_ORDER_LOCK, value.toString());
                //判断重新设置锁时有没有进程先抢占了这个锁
                if(getSetValue == null || StringUtils.equals(lockValue,getSetValue)){
                    //获取锁成功，可以进行业务操作
                    closeOrder(Const.RedisLock.CLOSE_ORDER_LOCK);
                }else{
                    log.info("没有获取到分布式锁 {}" ,Const.RedisLock.CLOSE_ORDER_LOCK);
                }
            }
        }
    }


    /**
     * 业务操作，关闭订单，先设置超时时间，完成后删除锁
     * */
    private void closeOrder(String lockName){
        ShardedRedisUtil.expire(lockName,5);
        log.info("获取到锁:{}，Thread:{}",Const.RedisLock.CLOSE_ORDER_LOCK);
        iOrderService.closeOrder(PropertiesUtil.getIntegerProperty("task.closeOrder","2"));
        ShardedRedisUtil.del(lockName);
    }


}
