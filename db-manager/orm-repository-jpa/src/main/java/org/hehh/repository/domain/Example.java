package org.hehh.repository.domain;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @author: HeHui
 * @date: 2020-08-03 16:40
 * @description: jpa Example 查询对象
 */
public class Example<T> {


    private final Restrictions<T> restrictions = new Restrictions<>();

    private Criterion<T> criterion;

    private Pageable pageable;

    private Sort sort;


    /**
     * 得到条件构造器
     *
     * @return {@link Criterion<T>}
     */
    public org.hehh.repository.domain.Criterion<T> getCriterion() {
        return criterion;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public Sort getSort() {
        return sort;
    }

    /**
     * 创建条件构造器
     *
     * @return {@link Restrictions<T>}
     */
    public Criterion<T> criteria(){
        Criterion<T> criterion = new Criterion<>(restrictions, true);
        if(this.criterion == null){
            this.criterion = criterion;
        }
        return criterion;
    }


    /**
     * 或
     *
     * @return {@link Criterion<T>}
     */
    public Criterion<T> or(){
        Criterion<T> criteria = new Criterion<>(restrictions, true);

        Criterion<T> or = new Criterion<>(restrictions,false);

        if(this.criterion != null){
            or.add(criterion);
        }

        or.add(criteria);

        this.criterion = or;

        return criteria;
    }




    /**
     * 和
     *
     * @return {@link Criterion<T>}
     */
    public Criterion<T> and(){

        Criterion<T> criteria = new Criterion<>(restrictions, true);

        Criterion<T> and = new Criterion<>(restrictions,true);

        if(this.criterion != null){
            and.add(criterion);
        }

        and.add(criteria);

        this.criterion = and;

        return criteria;
    }





    /**
     *  分页
     *  @param page 页面
     * @param size 大小
     * @return
     */
    public Pageable page(int page, int size){
        this.pageable = PageRequest.of(page,size);
        return this.pageable;
    }



    /**
     * 分页
     *
     * @param pageDto 分页dto
     */
    public Pageable page(PageDto pageDto){
       return page(pageDto.getPageNumber(),pageDto.getPageSize());
    }





    
    static class Criterion<T> implements org.hehh.repository.domain.Criterion<T> {

        private final List<org.hehh.repository.domain.Criterion<T>> criterionList = new LinkedList<>();


        private final org.hehh.repository.domain.Criterion<T> criterion;


        private final Restrictions<T> restrictions;


        private void add(org.hehh.repository.domain.Criterion<T> criterion){
            criterionList.add(criterion);
        }


        Criterion(Restrictions<T> restrictions,boolean and) {
            this.restrictions = restrictions;
            this.criterion = new LogicalExpression<>(and ? Operator.AND : Operator.OR,criterionList);
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
            return criterion.toPredicate(root,query,builder);
        }


        /**
         * 等于
         */
        public Criterion<T> eq(String fieldName, Object value) {
            org.hehh.repository.domain.Criterion<T> expression = restrictions.eq(fieldName,value);
            add(expression);
            return this;
        }


        /**
         * 集合包含某个元素
         */
        public Criterion<T> in(String fieldName, Object... value) {
            org.hehh.repository.domain.Criterion<T> criterion = restrictions.in(fieldName, value);
            add(criterion);
            return this;
        }
        /**
         * 包含于
         */
        public Criterion<T> in(String fieldName, Collection<Object> value) {
            org.hehh.repository.domain.Criterion<T> criterion = restrictions.in(fieldName, value);
            add(criterion);
            return this;
        }



        /**
         * 不包含于
         */
        public Criterion<T> notIn(String fieldName, Collection<Object> value) {
            org.hehh.repository.domain.Criterion<T> criterion = restrictions.notIn(fieldName, value);
            add(criterion);
            return this;
        }



        /**
         * 不包含于
         */
        public Criterion<T> notIn(String fieldName, Object... value) {
            org.hehh.repository.domain.Criterion<T> criterion = restrictions.notIn(fieldName, value);
            add(criterion);
            return this;
        }




        /**
         * 不等于
         */
        public Criterion<T> notEq(String fieldName, Object value) {
            org.hehh.repository.domain.Criterion<T> criterion = restrictions.ne(fieldName, value);
            add(criterion);
            return this;
        }


        /**
         * 模糊匹配
         */
        public Criterion<T> like(String fieldName, String value) {
            org.hehh.repository.domain.Criterion<T> criterion = restrictions.like(fieldName, value);
            add(criterion);
            return this;
        }




        /**
         * 大于
         */
        public Criterion<T> gt(String fieldName, Object value) {
            org.hehh.repository.domain.Criterion<T> criterion = restrictions.gt(fieldName, value);
            add(criterion);
            return this;
        }

        /**
         * 小于
         */
        public Criterion<T> lt(String fieldName, Object value) {
            org.hehh.repository.domain.Criterion<T> criterion = restrictions.lt(fieldName, value);
            add(criterion);
            return this;
        }

        /**
         * 小于等于
         */
        public Criterion<T> le(String fieldName, Object value) {
            org.hehh.repository.domain.Criterion<T> criterion = restrictions.lte(fieldName, value);
            add(criterion);
            return this;
        }



        /**
         * 大于等于
         */
        public Criterion<T> ge(String fieldName, Object value) {
            org.hehh.repository.domain.Criterion<T> criterion = restrictions.gte(fieldName, value);
            add(criterion);
            return this;
        }




    }







}
