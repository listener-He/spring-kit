package org.hehh.security.allowlist;

/**
 * @author: HeHui
 * @date: 2020-06-22 10:43
 * @description: 简单白名单
 */
public class SimpleAllowList implements AllowList {



    private final AllowListOperation allowListOperation;



    public SimpleAllowList(AllowListOperation allowListOperation){
        assert allowListOperation != null : "白名单操作类不能为空";
        this.allowListOperation = allowListOperation;
    }





    /**
     * 是否在白名单中
     *
     * @param ip 客户端地址
     * @return
     */
    @Override
    public boolean accept(String ip) {
        return allowListOperation.existing(ip);
    }
}
