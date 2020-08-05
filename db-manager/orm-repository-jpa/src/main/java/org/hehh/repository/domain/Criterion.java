package org.hehh.repository.domain;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author: HeHui
 * @date: 2020-08-03 17:20
 * @description: 条件表达式接口
 */
public interface Criterion<T> {














    /**
     *  转谓词
     * @param root Root接口，代表查询的根对象，可以通过root获取实体中的属性
     * @param query 代表一个顶层查询对象，用来自定义查询
     * @param builder 用来构建查询，此对象里有很多条件方法
     * @return
     */
    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
                          CriteriaBuilder builder);







}
