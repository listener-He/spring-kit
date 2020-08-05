package org.hehh.security.allowlist;

import org.hehh.security.IPUtil;
import org.hehh.security.IpRule;
import org.hehh.security.NotDelException;
import org.hehh.security.SubnetUtil;
import com.google.common.base.Strings;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-06-22 09:49
 * @description: 本地布隆过滤器白名单操作
 */
public class LocalBloomFilterAllowListOperation implements AllowListOperation {




    /**
     *  布隆过滤器
     */
    private final BloomFilter<Integer> allowListRuleBloomFilter;




    /**
     * 构造器1
     * @param expectSize 预计持久化黑名单大小
     * @param fpp 误中率 如 0.03 百分之三
     * @see BloomFilter
     */
    public LocalBloomFilterAllowListOperation(int expectSize,double fpp){
        this(BloomFilter.create(Funnels.integerFunnel(),expectSize,fpp));
    }





    /**
     *  构造器2
     * @param bloomFilter 布隆过滤器
     */
    public LocalBloomFilterAllowListOperation(BloomFilter<Integer> bloomFilter){
        this.allowListRuleBloomFilter = bloomFilter;
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
                        allowListRuleBloomFilter.put(v);
                    });
                }
            }else{
                allowListRuleBloomFilter.put(IPUtil.toInt(rule.getIp()));
            }
        }
    }





    /**
     * 移除一个白名单规则
     *
     * @param rule 规则
     */
    @Override
    public void remove(IpRule rule) throws NotDelException {
        throw new NotDelException("布隆过滤器不能移除元素");
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
            return allowListRuleBloomFilter.mightContain(IPUtil.toInt(ip));
        }
        return false;
    }
}
