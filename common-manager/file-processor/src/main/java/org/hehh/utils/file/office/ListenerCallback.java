package org.hehh.utils.file.office;

/**
 * @author: HeHui
 * @date: 2020-08-22 14:35
 * @description: 监听回调
 */
public interface ListenerCallback<T> {


    /**
     * 调用
     *
     * @param data  数据
     * @param index 指数
     */
    void invoke(T data,int index);


    /**
     * 结束
     */
    default void doAfter(){}
}
