package com.lingshi.shopping_seckill_service.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonLock {

    @Autowired
    private RedissonClient client;

    /**
     * 加锁
     *
     * @param key        加锁的key
     * @param expireTime 锁过期时间
     * @return true 加锁成功 false ，加锁失败
     */
    public boolean lock(String key, Long expireTime) {
        //获取锁
        RLock lock = client.getLock("lock_" + key);
        try {
            /**
             * 尝试获取锁  setnx lock_123213  ttl 1000
             * 如果抢到锁，则执行后续操作，如果没有抢到，则等待，如果抢到锁以后正常执行程序以后，则释放锁
             * 过期时间：解决，如果某个线程抢到锁以后，程序突然异常，没有正常释放锁，会造成死锁，整个程序无法执行，到期后自动释放
             */
            return lock.tryLock(expireTime, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            //程序出现异常，打断当前程序
            Thread.currentThread().interrupt();
            return false;
        }

    }

    /**
     * 释放锁
     * @param key 锁的key
     */
    public void  unlock(String key){
        //获取锁
        RLock lock = client.getLock("lock_" + key);
        //如果锁被锁住，则释放锁
        if(lock.isLocked()){
            lock.unlock();
        }
    }
}