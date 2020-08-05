package org.hehh.cloud.cache.topic;

/**
 * @author: HeHui
 * @create: 2019-09-27 10:51
 * @description: 通知类型
 **/
public enum NoticeType implements java.io.Serializable {

    /**
     *  添加or更改
     */
    PUT,

    /**
     *  清除全部
     */
    CLEAR,

    /**
     *  删除某个
     */
    EVICT

}
