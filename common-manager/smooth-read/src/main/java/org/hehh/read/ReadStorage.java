package org.hehh.read;

import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-07-20 14:57
 * @description: 阅读数存储
 */
public interface ReadStorage<T extends Number,ID>  {

    /**
     * 增加
     *
     * @param key 关键
     * @param n   阅读数
     */
    void increase(ID key,T n);


    /**
     * 增加
     *
     * @param data 数据
     */
    void increase(Map<ID,T> data);
}
