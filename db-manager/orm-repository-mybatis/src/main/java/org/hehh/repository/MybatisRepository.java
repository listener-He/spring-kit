package org.hehh.repository;

import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.entity.Example;

/**
 * @author: HeHui
 * @date: 2020-05-05 21:52
 * @description: mybatis 资源库
 */
public interface MybatisRepository<T,I> extends Mapper<T>, InsertListMapper<T>,Repository<T,I, Example> {
}
