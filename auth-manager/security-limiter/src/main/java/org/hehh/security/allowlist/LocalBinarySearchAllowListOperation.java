package org.hehh.security.allowlist;

import com.google.common.base.Strings;
import org.hehh.security.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-06-19 16:27
 * @description: 本地二分查找白名单操作
 */
public class LocalBinarySearchAllowListOperation  implements AllowListOperation {



    /**
     *  白名单规则
     */
    private final List<Integer> allowListRules;




    /**
     * 构造器1
     * @param expectSize 初始化白名单大小
     */
    public LocalBinarySearchAllowListOperation(int expectSize){
        this(new ArrayList<>(expectSize));
    }





    /**
     *  构造器2
     * @param allowListRules 白名单
     */
    public LocalBinarySearchAllowListOperation(List<Integer> allowListRules){
        if(allowListRules != null && !allowListRules.isEmpty()){
            Collections.sort(allowListRules);
        }
        this.allowListRules = allowListRules;
    }






    /**
     * 添加一个白名单规则
     *
     * @param rule 规则
     */
    @Override
    public void put(IpRule rule) {
        if(null != rule && IPUtil.isIp(rule.getIp())){

            /**
             *  是否cidr
             */
            if(Strings.isNullOrEmpty(rule.getCidrPrefix())){
                 String cidr = rule.getIp()+"/"+rule.getCidrPrefix();
                 if(IPUtil.isCidr(cidr)){
                     /**
                      *  解析cidr的ip
                      */
                     SubnetUtil utils = new SubnetUtil(cidr);
                     List<Integer> ips = Arrays.asList(utils.getInfo().getAllAddresses()).stream().map(v -> IPUtil.toInt(v)).collect(Collectors.toList());
                        ips.forEach(v->{
                            add(v);
                        });
                 }
             }else{
                add(IPUtil.toInt(rule.getIp()));
            }
        }
    }




    /**
     *  有序插入
     * @param target
     */
    private void add(int target){
        int i = CollectionUtil.binSearchIndex(allowListRules, target);
        if(i >= 0){
            allowListRules.add(i,target);
        }
    }






    /**
     * 移除一个白名单规则
     *
     * @param rule 规则
     */
    @Override
    public void remove(IpRule rule) throws NotDelException {
        if(null != rule || IPUtil.isIp(rule.getIp())){

            /**
             *  是否cidr
             */
            if(Strings.isNullOrEmpty(rule.getCidrPrefix())){
                String cidr = rule.getIp()+"/"+rule.getCidrPrefix();
                if(IPUtil.isCidr(cidr)){
                    /**
                     *  解析cidr的ip
                     */
                    SubnetUtil utils = new SubnetUtil(cidr);
                    List<Integer> ips = Arrays.asList(utils.getInfo().getAllAddresses()).stream().map(v -> IPUtil.toInt(v)).collect(Collectors.toList());
                    ips.forEach(v->{
                        allowListRules.remove(v);
                    });
                }
            }else{
                allowListRules.remove(IPUtil.toInt(rule.getIp()));
            }
        }
    }






    /**
     * 查询一个规则
     *
     * @param ip
     * @return
     */
    @Override
    public boolean existing(String ip) {
        if(IPUtil.isIp(ip)){
            /**
             *  永久化中查询
             */
            return Collections.binarySearch(allowListRules, IPUtil.toInt(ip)) >= 0;

        }
        return false;
    }
}
