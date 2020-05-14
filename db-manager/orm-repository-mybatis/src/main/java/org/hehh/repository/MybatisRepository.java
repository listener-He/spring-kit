package org.hehh.repository;

import tk.mybatis.mapper.common.Mapper;

/**
 * @author: HeHui
 * @date: 2020-05-05 21:52
 * @description: mybatis 资源库
 */
public interface MybatisRepository<T> extends Mapper<T>,Repository<T> {
}
