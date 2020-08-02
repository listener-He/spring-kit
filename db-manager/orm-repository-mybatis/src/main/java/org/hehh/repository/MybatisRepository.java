package org.hehh.repository;

import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-05-05 21:52
 * @description: mybatis 资源库
 */
public interface MybatisRepository<T,I> extends Mapper<T>, InsertListMapper<T>,Repository<T,I, Example> {

    /**
     * 批量更新
     *
     * @param recordList 记录列表
     * @return int
     */
    int updateList(List<? extends T> recordList);


    /**
     *  批量选择性更新
     *
     * @param recordList 记录列表
     * @return int
     */
    int updateListSelective(List<? extends T> recordList);

}
