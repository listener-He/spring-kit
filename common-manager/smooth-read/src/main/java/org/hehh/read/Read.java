package org.hehh.read;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-07-20 11:51
 * @description: 阅读器
 */
public interface Read<T extends Number,ID> {



    /**
     *  读取
     * @param key 阅读的key
     * @param n 增加数
     * @param clientID 客户端ID （非必填）
     */
    void read(ID key,T n,String clientID);


    /**
     *  查询指定key的阅读数
     * @param key 阅读key
     * @return {@link Optional<T>}
     */
    Optional<T> getRead(ID key);


    /**
     *  查询多个key的阅读数
     * @param keys 阅读keys
     * @return {@link Optional<Map<ID,T>>}
     */
    Optional<Map<ID,T>> getReads(List<ID> keys);
}
