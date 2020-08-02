package org.hehh.repository.service.impl;

import org.hehh.repository.DbJpaRepository;
import org.hehh.repository.domain.Page;
import org.hehh.repository.service.IJpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-05-31 20:41
 * @description: spring-data 数据库 业务 实现
 */
public class JpaServiceImpl<T,ID> implements IJpaService<T,ID> {



    @Autowired
    private  DbJpaRepository<T,ID> repository;







    /**
     * 获取条件构造器
     *
     * @return
     */
    @Override
    public Example<T> getExample(T t) {
        return Example.of(t);
    }


    /**
     * 根据条件查询：
     * 参数：是一个Example条件对象
     *
     * @param example 条件构造器
     * @return {@link List<T>}
     */
    @Override
    public List<T> findListWhere(Example<T> example) {
        return repository.findAll(example);
    }

    /**
     * 查询一个
     *
     * @param example 条件构造器
     * @return {@link Optional}
     */
    @Override
    public Optional<T> findOneWhere(Example<T> example) {
        return repository.findOne(example);
    }

    /**
     * 根据条件查询总记录数
     *
     * @param example 条件构造器
     * @return
     */
    @Override
    public long findCountWhere(Example<T> example) {
        return repository.count(example);
    }



    /**
     * 分页查询
     *
     * @param page 当前页
     * @param rows 页大小
     * @return
     */
    @Override
    public Page<T> findPage(int page, int rows) {
       return (Page<T>) repository.findAll(PageRequest.of(page, rows));
    }



    /**
     * 分页查询
     *
     * @param page    当前页
     * @param rows    页大小
     * @param example 条件构造器
     * @return
     */
    @Override
    public Page<T> findPage(int page, int rows, Example<T> example) {
        return (Page<T>) repository.findAll(example,PageRequest.of(page, rows));
    }




    /**
     * 是否存在ID
     *
     * @param id
     * @return
     */
    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }






    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * 根据ID查询
     *
     * @param ids
     * @return
     */
    @Override
    public List<T> findInIds(List<ID> ids) {
        return repository.findAllById(ids);
    }



    /**
     * 根据ID查询单个
     *
     * @param id
     * @return
     */
    @Override
    public Optional<T> findOne(ID id) {
        return repository.findById(id);
    }


    /**
     * 选择性新增
     *
     * @param t
     * @return
     */
    @Override
    public int saveSelective(T t) {
       return 0;//this.save(t);
    }

    /**
     * 保存记录
     *
     * @param t
     * @return
     */
    @Override
    public int save(T t) {
        T save = repository.save(t);
        if(save != null){
            return 1;
        }
        return 0;
    }



    /**
     * 批量保存（不能忽略null）
     *
     * @param t
     * @return
     */
    @Override
    public int batchSave(List<T> t) {
        List<T> ts = repository.saveAll(t);
        if(CollectionUtils.isEmpty(ts)){
            return 0;
        }

        return ts.size();
    }



    /**
     * 根据ID选择性更新 忽略 null
     *
     * @param t 实体
     */
    @Override
    public int updateSelective(T t) {
        return 0;
    }

    /**
     * 根据ID更改
     *
     * @param t 实体
     * @return
     */
    @Override
    public int update(T t) {
        T save = repository.save(t);

        return save == null ? 0 : 1;
    }


    /**
     * 根据条件删除
     *
     * @param t 实体
     * @return 影响条数
     */
    @Override
    public int delete(T t) {
         repository.delete(t);
         return 1;
    }



    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public int deleteById(ID id) {
        repository.deleteById(id);
        return 1;
    }

    /**
     * 根据id集合批量删除
     *
     * @param ids id集合
     * @return 删除条数
     */
    @Override
    public int deleteInId(ID... ids) {
        return this.deleteInId(Arrays.asList(ids));
    }

    /**
     * 批量删除
     *
     * @param ids id集合
     * @return 删除条数
     */
    @Override
    public int deleteInId(List<ID> ids) {
        repository.deleteByIdIn(ids);
        return ids.size();
    }
}
