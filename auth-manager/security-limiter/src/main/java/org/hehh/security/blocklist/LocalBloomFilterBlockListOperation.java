package org.hehh.security.blocklist;

import org.hehh.security.IpRule;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;

/**
 * @author: HeHui
 * @date: 2020-06-18 11:11
 * @description: 本地布隆过滤黑名单操作
 */
public class LocalBloomFilterBlockListOperation extends LocalMapBlockListAbstract<String> implements BlockListOperation {


    /**
     *  布隆过滤器
     */
    private final BloomFilter<String> ipFilterTimeRuleBloomFilter;


    /**
     *   过期时间超过本时间视为持久时间
     */
    public static final long persistentTime = 315360000000L;

    /**
     * 构造器1
     * @param expectSize 预计持久化黑名单大小
     * @param fpp 误中率 如 0.03 百分之三
     * @see BloomFilter
     */
    public LocalBloomFilterBlockListOperation(int expectSize,double fpp){
        this(BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()),expectSize,fpp));
    }


    /**
     *  构造器2
     * @param bloomFilter 布隆过滤器
     */
    public LocalBloomFilterBlockListOperation(BloomFilter<String> bloomFilter){
        this.ipFilterTimeRuleBloomFilter = bloomFilter;
    }






    /**
     * 添加一个黑名单规则
     *
     * @param rule 规则
     */
    @Override
    public void put(BlockIpRule rule) {
        if(rule != null){

            /**
             *  是否持久化
             */
            if(rule.getExpirationTime() - System.currentTimeMillis()  >= persistentTime){
                ipFilterTimeRuleBloomFilter.put(rule.getIp());
            }else{
                /**
                 *  存储到临时
                 */
                super.put(rule.getIp(),rule.getExpirationTime());
            }

        }
    }




    /**
     * 移除一个黑名单规则
     *
     * @param rule 规则
     */
    @Override
    public void remove(BlockIpRule rule) {
        if(null != rule){
            super.remove(rule.getIp());
        }
    }





    /**
     * 延长规则时间
     *
     * @param rule 规则
     * @param ms   毫秒
     */
    @Override
    public void extended(IpRule rule, int ms) {
        if(rule != null){
            /**
             *  临时map是否存在
             */
            Long aLong = super.get(rule.getIp());
            if(aLong == null){
                super.put(rule.getIp(),ms);
                return;
            }

            /**
             *  是否超过持久化判断
             */
            if(aLong + ms >= System.currentTimeMillis()+persistentTime){
                ipFilterTimeRuleBloomFilter.put(rule.getIp());
                super.remove(rule.getIp());
                return;
            }

            super.put(rule.getIp(),aLong+ms);
        }
    }





    /**
     * 查询一个黑名单规则
     *
     * @param ip
     * @return
     */
    @Override
    public BlockIpRule find(String ip) {
        if(null == ip){
            return null;
        }

        /**
         *  永久化中查询
         */
        if(ipFilterTimeRuleBloomFilter.mightContain(ip)){
            BlockIpRule rule = new BlockIpRule();
            rule.setIp(ip);
            rule.setExpirationTime(System.currentTimeMillis()+persistentTime);
            return rule;
        }

        /**
         *  临时中查询
         */
        Long aLong = super.get(ip);
        if(aLong != null){
            BlockIpRule rule = new BlockIpRule();
            rule.setIp(ip);
            rule.setExpirationTime(aLong);
            return rule;
        }
        return null;
    }

}
