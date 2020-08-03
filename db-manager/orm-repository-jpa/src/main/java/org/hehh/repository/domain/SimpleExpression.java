package org.hehh.repository.domain;

import org.springframework.util.Assert;

import javax.persistence.criteria.*;

/**
 * @author: HeHui
 * @date: 2020-08-03 17:39
 * @description: 简单的条件条件表达式
 */
public class SimpleExpression<T> implements Criterion<T> {


    /**
     * 属性名
     */
    private  String fieldName;

    /**
     * 值
     */
    private  Object value;

    /**
     * 条件符
     */
    private  Operator operator;







    protected SimpleExpression(String fieldName, Object value, Operator operator) {
        Assert.hasText(fieldName,"属性名不能为空");
        Assert.notNull(operator,"条件符不能为空");

        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }







    /**
     * 转谓词
     *
     * @param root    Root接口，代表查询的根对象，可以通过root获取实体中的属性
     * @param query   代表一个顶层查询对象，用来自定义查询
     * @param builder 用来构建查询，此对象里有很多条件方法
     * @return
     */
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Expression expression = root.get(fieldName);

        switch (operator) {
            case EQ:
                return builder.equal(expression, value);
            case NOT_EQ:
                return builder.notEqual(expression, value);
            case LIKE:
                return builder.like((Expression<String>) expression,  (String) value);
            case LT:
                return builder.lessThan(expression, (Comparable) value);
            case GT:
                return builder.greaterThan(expression, (Comparable) value);
            case LE:
                return builder.lessThanOrEqualTo(expression, (Comparable) value);
            case GE:
                return builder.greaterThanOrEqualTo(expression, (Comparable) value);
            case IN:
                return builder.isMember(value, expression);
            case NOT_IN:
                return builder.isNotMember(value, expression);
            default:
                return null;
        }
    }













}
