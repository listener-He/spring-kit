package org.hehh.repository;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-05-04 21:02
 * @description: spring-data-jpa 资源库
 */
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
}
