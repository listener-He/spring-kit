package org.hehh.repository.domain;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-08-03 16:40
 * @description: jpa Example 查询对象
 */
public class Example<T> {


    private Restrictions<T> restrictions;


    private Criterion<T> criterion;




    /**
     * 创建谓词工厂
     *
     * @return {@link Restrictions<T>}
     */
    public Restrictions<T> buildCriteria(){
        if(restrictions == null){
            restrictions = new Restrictions<>();
        }
        return restrictions;
    }





    static class ExampleCriterion<T> implements  Criterion<T>{

        private Set<Criterion<T>> criterionSet = new HashSet<>();


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
            return builder.and(criterionSet.stream().map(v-> v.toPredicate(root,query,builder)).collect(Collectors.toList()).toArray(new Predicate[0]));
        }
    }







}
