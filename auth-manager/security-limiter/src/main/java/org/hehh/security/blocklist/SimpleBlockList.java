package org.hehh.security.blocklist;


/**
 * @author: HeHui
 * @date: 2020-06-17 11:52
 * @description: 简单的二分查找 黑名单
 */
public class SimpleBlockList implements BlockList {



    private final BlockListOperation blockListOperation;


    /**
     *  构造器
     * @param blockListOperation
     */
    public SimpleBlockList(BlockListOperation blockListOperation){

        assert  blockListOperation != null : "黑名单操作类不能为空";

        this.blockListOperation = blockListOperation;
    }




    /**
     * 是否在黑名单中
     *
     * @param ip 客户端地址
     * @return
     */
    @Override
    public boolean accept(String ip) {
        if(ip == null){
            return false;
        }
        BlockIpRule rule = blockListOperation.find(ip);
        return  rule != null && rule.getExpirationTime() > System.currentTimeMillis();
    }
}
