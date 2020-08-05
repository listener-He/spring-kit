package org.hehh.repository.domain;

/**
 * @author: HeHui
 * @date: 2020-08-03 19:29
 * @description: 条件符
 */
public enum  Operator {


    /**
     *  包含
     */
    IN,

    /**
     *  模糊
     */
    LIKE,

    /**
     *  等于 equal
     */
    EQ,

    /**
     * 不等于
     */
    NOT_EQ,

    /**
     *  小于
     */
    LT,

    /**
     *  小于=
     */
    LE,

    /**
     * 大于
     */
    GT,

    /**
     *  大于=
     */
    GE,

    AND,

    OR,

    NOT_IN
}
