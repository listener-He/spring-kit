package org.hehh.repository.domain;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-08-03 19:35
 * @description:
 */
public class LogicalExpression<T> implements Criterion<T> {

    /**
     * 条件符
     */
    private final Operator operator;


    private final List<Criterion<T>> criteria;


    public LogicalExpression(Operator operator, Criterion<T>... criteria) {
       this(operator,Arrays.asList(criteria));
    }


    public LogicalExpression(Operator operator, List<Criterion<T>> criteria) {
        this.operator = operator;
        this.criteria = criteria;
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

        if(operator.equals(Operator.AND) || operator.equals(Operator.OR)){

            Predicate[] predicates = criteria.stream().map(v -> v.toPredicate(root,query,builder)).collect(Collectors.toList()).toArray(new Predicate[0]);
            if(operator.equals(Operator.AND)){
                return builder.and(predicates);
            }
            return builder.or(predicates);
        }
        return null;
    }
}
