package org.hehh.repository;


import java.util.Collection;
import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-05-04 20:56
 * @description: 数据资源库
 */
public interface Repository<T,I,W> {


//    /**
//     * 插入一条记录
//     *
//     * @param entity 实体对象
//     */
//    int insert(T entity);
//
//
//    /**
//     * 根据 ID 删除
//     *
//     * @param id 主键ID
//     */
//    int deleteById(I id);
//
//
//    /**
//     * 删除（根据ID 批量删除）
//     *
//     * @param idList 主键ID列表(不能为 null 以及 empty)
//     */
//    int deleteBatchIds(Collection<I> idList);
//
//
//    /**
//     * 根据 ID 修改
//     *
//     * @param entity 实体对象
//     */
//    int updateById(T entity);
//
//
//    /**
//     * 根据 entity 条件，删除记录
//     *
//     * @param wrapper 实体对象封装操作类（可以为 null）
//     */
//    int deleteByWrapper(W wrapper);
//
//
//
//    /**
//     * 根据 whereEntity 条件，更新记录
//     *
//     * @param entity        实体对象 (set 条件值,可以为 null)
//     * @param updateWrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
//     */
//    int updateByWrapper( T entity, W updateWrapper);
//
//
//
//    /**
//     * 根据 ID 查询
//     *
//     * @param id 主键ID
//     */
//    T findById(I id);
//
//    /**
//     * 查询（根据ID 批量查询）
//     *
//     * @param idList 主键ID列表(不能为 null 以及 empty)
//     */
//    List<T> findInIds(Collection<I> idList);
//
//
//
//    /**
//     * 根据 entity 条件，查询一条记录
//     *
//     * @param queryWrapper 实体对象封装操作类（可以为 null）
//     */
//    T findOne(W queryWrapper);
//
//
//
//    /**
//     * 根据 Wrapper 条件，查询总记录数
//     *
//     * @param queryWrapper 实体对象封装操作类（可以为 null）
//     */
//    Integer getCount(W queryWrapper);
//
//
//    /**
//     * 根据 entity 条件，查询全部记录
//     *
//     * @param queryWrapper 实体对象封装操作类（可以为 null）
//     */
//    List<T> findList(W queryWrapper);




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
