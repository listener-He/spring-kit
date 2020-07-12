package org.hehh.security.blocklist;

import org.hehh.security.IpRule;
import org.hehh.security.NotDelException;

/**
 * @author: HeHui
 * @date: 2020-06-17 11:28
 * @description: 黑名单操作类
 */
public interface BlockListOperation {


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
}
