package org.hehh.repository.service;

import org.hehh.repository.IDbService;
import org.hehh.repository.domain.Page;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-05-31 20:28
 * @description: spring-data 数据库 service
 */
public interface IJpaService<T,ID>  extends IDbService<T,ID> {




    /**
     *  获取条件构造器
     * @return
     */
    Example<T> getExample(T t);



    /**
     * 根据条件查询：
     * 参数：是一个Example条件对象
     *
     * @param example 条件构造器
     * @return {@link List<T>}
     */
    List<T> findListWhere(Example<T> example);


    /**
     *  查询一个
     * @param example 条件构造器
     * @return {@link Optional}
     */
    Optional<T> findOneWhere(Example<T> example);



    /**
     * 根据条件查询总记录数
     *
     * @param example 条件构造器
     * @return
     */
    long findCountWhere(Example<T> example);



    /**
     *   分页查询
     * @param page 当前页
     * @param rows 页大小
     * @return
     */
    Page<T> findPage(int page, int rows);


    /**
     *   分页查询
     * @param page 当前页
     * @param rows 页大小
     * @param example 条件构造器
     * @return
     */
    Page<T> findPage(int page, int rows,Example<T> example);






}
