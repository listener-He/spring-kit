package org.hehh.security.blocklist;

import io.netty.util.internal.StringUtil;
import org.hehh.security.IpRule;
import org.hehh.security.NotDelException;

/**
 * @author: HeHui
 * @date: 2020-06-17 11:28
 * @description: 黑名单操作类
 */
public interface BlockListOperation extends BlockList{


    /**
     *  添加一个黑名单规则
     * @param rule 规则
     */
    void put(BlockIpRule rule);


    /**
     *  移除一个黑名单规则
     * @param rule 规则
     */
    void remove(BlockIpRule rule) throws NotDelException;


    /**
     *  延长规则时间
     * @param rule 规则
     * @param ms 毫秒
     */
    void extended(IpRule rule,int ms);


    /**
     *  查询一个黑名单规则
     * @param ip
     * @return
     */
    BlockIpRule find(String ip);


    /**
     * 是否在黑名单中
     *
     * @param ip 客户端地址
     * @return
     */
    @Override
    default boolean accept(String ip){
        if(StringUtil.isNullOrEmpty(ip)){
            return false;
        }
        BlockIpRule rule = this.find(ip);
        return  rule != null && rule.getExpirationTime() > System.currentTimeMillis();
    }
}
