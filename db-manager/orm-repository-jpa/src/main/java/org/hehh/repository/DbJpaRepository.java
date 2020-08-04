package org.hehh.repository;

import org.hehh.repository.domain.Example;
import org.hehh.repository.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-05-04 21:02
 * @description: spring-data-jpa 资源库
 */
@NoRepositoryBean
public interface DbJpaRepository<T,ID>  extends JpaRepository<T,ID>, Repository<T,ID,Example<T>> {


    /**
     *  根据id删除
     * @param ids
     */
    int deleteByIdIn(List<ID> ids);


    /**
     * 根据主键更新
     *
     * @param entity 实体
     * @return int
     */
    <S extends T> int update(S entity);



    /**
     * 选择性更新，忽略null。根据主键更新
     *
     * @param entity 实体
     * @return int
     */
    <S extends T> int updateSelective(S entity);


    /**
     * 批量更新
     *
     * @param recordList 记录列表
     * @return int
     */
    @Override
    int updateList(List< T> recordList);

    /**
     * 批量选择性更新
     *
     * @param recordList 记录列表
     * @return int
     */
    @Override
    int updateListSelective(List<T> recordList);


    /**
     * 找到一个
     *
     * @param example 例子
     * @return {@link Optional<T>}
     */
    Optional<T> findOne(Example<T> example);


    /**
     * 找到多个
     *
     * @param example 例子
     * @return {@link Optional<T>}
     */
    List<T> findAll(Example<T> example);


    /**
     *  分页
     * @param example
     * @return
     */
    Page<T> findPage(Example<T> example);
}
