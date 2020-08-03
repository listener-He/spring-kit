package org.hehh.repository.domain;


import java.util.Collection;

/**
 * @author: HeHui
 * @date: 2020-08-03 19:09
 * @description: 谓词工厂
 */
public class Restrictions<T> {



    /**
     * 等于
     */
    public  Criterion<T> eq(String fieldName, Object value) {
        return new SimpleExpression<T>(fieldName, value, Operator.EQ);
    }


    /**
     * 集合包含某个元素
     */
    public  Criterion<T> in(String fieldName, Object... value) {
        return new SimpleExpression<T>(fieldName, value, Operator.IN);
    }
    /**
     * 包含于
     */
    public  Criterion<T> in(String fieldName, Collection<Object> value) {
        return new SimpleExpression<T>(fieldName,value, Operator.IN);
    }


    /**
     * 不包含于
     */
    public  Criterion<T> notIn(String fieldName, Collection<Object> value) {
        return new SimpleExpression<T>(fieldName,value, Operator.NOT_IN);
    }

    /**
     * 不包含于
     */
    public  Criterion<T> notIn(String fieldName, Object... value) {
        return new SimpleExpression<T>(fieldName, value, Operator.NOT_IN);
    }




    /**
     * 不等于
     */
    public  Criterion<T> ne(String fieldName, Object value) {
        return new SimpleExpression<T>(fieldName, value, Operator.NOT_EQ);
    }


    /**
     * 模糊匹配
     */
    public  Criterion<T> like(String fieldName, String value) {
        return new SimpleExpression<T>(fieldName, value, Operator.LIKE);
    }



    /**
     * 大于
     */
    public  Criterion<T> gt(String fieldName, Object value, boolean ignoreNull) {

        return new SimpleExpression<T>(fieldName, value, Operator.GT);
    }

    /**
     * 小于
     */
    public  Criterion<T> lt(String fieldName, Object value) {

        return new SimpleExpression<T>(fieldName, value, Operator.LT);
    }

    /**
     * 小于等于
     */
    public  Criterion<T> lte(String fieldName, Object value) {

        return new SimpleExpression<T>(fieldName, value, Operator.GE);
    }

    /**
     * 大于等于
     */
    public  Criterion<T> gte(String fieldName, Object value) {

        return new SimpleExpression<T>(fieldName, value, Operator.LE);
    }




    /**
     * 并且
     */
    public  Criterion<T> and(Criterion<T>... criterion) {
        return new LogicalExpression<T>(Operator.AND,criterion);
    }

    /**
     * 或者
     */
    public  Criterion<T> or(Criterion<T>... criterion) {
        return new LogicalExpression<>(Operator.OR,criterion);
    }

    
}
