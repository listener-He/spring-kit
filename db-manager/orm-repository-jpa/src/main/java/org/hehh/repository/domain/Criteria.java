package org.hehh.repository.domain;


/**
 * @author: HeHui
 * @date: 2020-08-03 16:42
 * @description: jpa Example 的条件
 */
public class Criteria {

    /**
     * 属性名
     */
    private final String propertyName
        ;


    private final Where where;


    private final Object value;


    /**
     * 标准
     *
     * @param propertyName 属性名
     * @param where        条件
     * @param value        值
     */
    public Criteria(String propertyName, Where where, Object value) {
        this.propertyName = propertyName;
        this.where = where;
        this.value = value;
    }
}
