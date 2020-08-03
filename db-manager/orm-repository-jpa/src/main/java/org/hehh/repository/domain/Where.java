package org.hehh.repository.domain;

/**
 * @author: HeHui
 * @date: 2020-08-03 15:59
 * @description: where 条件
 */
public enum Where {

    /**
     *  包含
     */
    IN,

    /**
     *  模糊
     */
    LIKE,

    /**
     *  等于
     */
    EQUAL,

    /**
     * 不等于
     */
    NOT_EQUAL,

    /**
     *  小于
     */
    LESS_THAN,

    /**
     *  小于=
     */
    LESS_THAN_OR_EQUAL_TO,

    /**
     * 大于
     */
    GREATER_THAN,
    /**
     *  大于=
     */
    GREATER_THAN_OR_EQUAL_TO

}
