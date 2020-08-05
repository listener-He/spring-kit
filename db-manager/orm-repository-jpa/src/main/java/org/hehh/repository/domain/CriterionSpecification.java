package org.hehh.repository.domain;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author: HeHui
 * @date: 2020-08-04 01:09
 * @description: Criterion 条件
 */
public class CriterionSpecification<T> implements Specification<T> {


    private final Criterion<T> criterion;


    public CriterionSpecification(Criterion<T> criterion) {
        Assert.notNull(criterion,"Criterion条件表达式不能为空");
        this.criterion = criterion;
    }


    /**
     * Creates a WHERE clause for a query of the referenced entity in form of a {@link Predicate} for the given
     * {@link Root} and {@link CriteriaQuery}.
     *
     * @param root            must not be {@literal null}.
     * @param query           must not be {@literal null}.
     * @param criteriaBuilder must not be {@literal null}.
     * @return a {@link Predicate}, may be {@literal null}.
     */
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criterion.toPredicate(root,query,criteriaBuilder);
    }
}
