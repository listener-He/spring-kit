package org.hehh.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;
import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-05-23 21:54
 * @description: mybatis-plus 资源库
 */
public interface MybatisPlusRepository<T,I> extends BaseMapper<T>,Repository<T,I, Wrapper<T>> {





}
