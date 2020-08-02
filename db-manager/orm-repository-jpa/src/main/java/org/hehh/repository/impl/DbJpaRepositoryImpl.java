package org.hehh.repository.impl;

import org.hehh.repository.DbJpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-08-03 00:41
 * @description: 自定义jap Repository实现 https://blog.csdn.net/zhoudingding/article/details/107206166
 */
public class DbJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements DbJpaRepository<T,ID> {

    /**
     * Creates a new {@link SimpleJpaRepository} to manage objects of the given {@link JpaEntityInformation}.
     *
     * @param entityInformation must not be {@literal null}.
     * @param entityManager     must not be {@literal null}.
     */
    public DbJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }



    /**
     * 根据id删除
     *
     * @param ids
     */
    @Override
    public void deleteByIdIn(List<ID> ids) {

    }

    /**
     * 批量更新
     *
     * @param recordList 记录列表
     * @return int
     */
    @Override
    public int updateList(List<T> recordList) {
        return 0;
    }

    /**
     * 批量选择性更新
     *
     * @param recordList 记录列表
     * @return int
     */
    @Override
    public int updateListSelective(List<T> recordList) {
        return 0;
    }
}
