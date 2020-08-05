package org.hehh.security.blocklist;

import org.hehh.security.CollectionUtil;
import org.hehh.security.IPUtil;
import org.hehh.security.IpRule;
import com.google.common.hash.BloomFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-06-18 11:11
 * @description: 本地二分查找法黑名单操作
 */
public class LocalBinarySearchBlockListOperation extends LocalMapBlockListAbstract<Integer> implements BlockListOperation {


    /**
     * 布隆过滤器
     */
    private final List<Integer> ipRules;


    /**
     * 过期时间超过本时间视为持久时间
     */
    public static final long persistentTime = 315360000000L;

    /**
     * 构造器1
     *
     * @param expectSize 初始化持久化黑名单大小
     * @see BloomFilter
     */
    public LocalBinarySearchBlockListOperation(int expectSize) {
        this(new ArrayList<>(expectSize));
    }


    /**
     * 构造器2
     *
     * @param ipRules 黑名单集合
     */
    public LocalBinarySearchBlockListOperation(List<Integer> ipRules) {
        assert ipRules != null : "IP存储List不能为null";
        if (!ipRules.isEmpty()) {
            Collections.sort(ipRules);
        }
        this.ipRules = ipRules;
    }


    /**
     * 二分查找预插入索引实现。
     * @param target 查找元素
     */
    private int binSearchIndex(int target) {
       return CollectionUtil.binSearchIndex(ipRules,target);
    }


    /**
     *  有序插入
     * @param ip
     */
    private void ipRulesPut(int ip) {
        int i = binSearchIndex(ip);
        if(i >= 0){
            ipRules.add(i,ip);
        }
    }


    /**
     * 添加一个黑名单规则
     *
     * @param rule 规则
     */
    @Override
    public void put(BlockIpRule rule) {
        if (rule != null) {

            /**
             *  是否持久化
             */
            if (rule.getExpirationTime() - System.currentTimeMillis() >= persistentTime) {
                ipRulesPut(ipv4ToByte4Int(rule.getIp()));
            } else {
                /**
                 *  存储到临时
                 */
                super.put(ipv4ToByte4Int(rule.getIp()), rule.getExpirationTime());
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
        if (null != rule) {
            if (rule.getExpirationTime() - System.currentTimeMillis() >= persistentTime) {
                ipRules.remove(ipv4ToByte4Int(rule.getIp()));
                return;
            }
            super.remove(ipv4ToByte4Int(rule.getIp()));
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
        if (rule != null) {
            /**
             *  临时map是否存在
             */
            Long aLong = super.get(ipv4ToByte4Int(rule.getIp()));
            if (aLong == null) {
                super.put(ipv4ToByte4Int(rule.getIp()), ms);
                return;
            }

            /**
             *  是否超过持久化判断
             */
            if (aLong + ms >= System.currentTimeMillis() + persistentTime) {
                ipRulesPut(ipv4ToByte4Int(rule.getIp()));
                super.remove(ipv4ToByte4Int(rule.getIp()));
                return;
            }

            super.put(ipv4ToByte4Int(rule.getIp()), aLong + ms);
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
        if (null == ip) {
            return null;
        }

        /**
         *  永久化中查询
         */
        int i = Collections.binarySearch(ipRules, ipv4ToByte4Int(ip));
        if (i >= 0) {
            BlockIpRule rule = new BlockIpRule();
            rule.setIp(ip);
            rule.setExpirationTime(System.currentTimeMillis() + persistentTime);
            return rule;
        }

        /**
         *  临时中查询
         */
        Long aLong = super.get(ipv4ToByte4Int(ip));
        if (aLong != null) {
            BlockIpRule rule = new BlockIpRule();
            rule.setIp(ip);
            rule.setExpirationTime(aLong);
            return rule;
        }
        return null;
    }


    /**
     * ipv4转4字节 int
     *
     * @param ip
     * @return
     */
    private int ipv4ToByte4Int(String ip) {
        return IPUtil.toInt(ip);
    }




}
