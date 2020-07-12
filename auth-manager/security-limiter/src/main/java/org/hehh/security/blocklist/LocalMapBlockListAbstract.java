package org.hehh.security.blocklist;

import org.hehh.security.IpRuleRefresh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: HeHui
 * @date: 2020-06-18 17:33
 * @description: 本地map存储黑名单规则
 */
public abstract class LocalMapBlockListAbstract<K> implements IpRuleRefresh {


    /**
     *  临时存储
     */
    private final Map<K,Long> tempIpRule;


    /**
     *  默认使用 {@link ConcurrentHashMap} 存储 初始化大小为 512
     */
    public LocalMapBlockListAbstract(){
        this.tempIpRule = new ConcurrentHashMap<K,Long>(512);
    }


    /**
     *  传入一个{@link Map} 的实现用于存储临时规则
     * @param tempIpRule hash存储
     */
    public LocalMapBlockListAbstract(Map<K,Long> tempIpRule){
        assert tempIpRule != null : "存储map不能为空";
        this.tempIpRule = tempIpRule;
    }




    /**
     *  添加
     * @param rule 规则
     * @param expirationTime 过期时间
     */
    protected void put(K rule,long expirationTime){

        /**
         *  如果之前存在
         */
        if(tempIpRule.containsKey(rule)){
            Long aLong = tempIpRule.get(rule);
            long millis = System.currentTimeMillis();
            if(aLong != null && aLong > millis){
                expirationTime = expirationTime + (aLong - millis);
            }
        }
        tempIpRule.put(rule,expirationTime);
    }




    /**
     *  移除
     * @param rule 规则
     */
    protected void remove(K rule){
        tempIpRule.remove(rule);
    }


    /**
     * 延长规则时间
     *
     * @param rule 规则
     * @param ms   毫秒
     */
    protected void extended(K rule, int ms) {
        Long aLong = tempIpRule.get(rule);
        if(aLong != null){
           tempIpRule.put(rule, aLong + ms);
        }
    }





    /**
     *  获取
     * @param rule ip规则
     * @return
     */
    protected Long get(K rule){
        return tempIpRule.get(rule);
    }




    /**
     * 执行
     */
    @Override
    public void run() {
        if(!tempIpRule.isEmpty()){
            /**
             *  删除过期规则
             */
            long millis = System.currentTimeMillis();
            tempIpRule.entrySet().removeIf(v-> v.getValue() == null || v.getValue() < millis);
        }
    }
}
