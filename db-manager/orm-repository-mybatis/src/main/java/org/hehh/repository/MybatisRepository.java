package org.hehh.repository;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.additional.insert.InsertListProvider;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-05-05 21:52
 * @description: 通用mapper 资源库
 */
public interface MybatisRepository<T,I> extends Mapper<T>, InsertListMapper<T>,Repository<T,I, Example> {


    /**
     * 批量更新
     *
     * @param recordList 记录列表
     * @return int
     */
    @Override
    @InsertProvider(type = MybatisRepositoryProvider.class, method = "dynamicSQL")
    int updateList(List<T> recordList);

    /**
     *  批量选择性更新
     *
     * @param recordList 记录列表
     * @return int
     */
    @Override
    @InsertProvider(type = MybatisRepositoryProvider.class, method = "dynamicSQL")
    int updateListSelective(List<T> recordList);

}
