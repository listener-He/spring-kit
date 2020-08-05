package org.hehh.repository.service.impl;

import com.github.pagehelper.PageHelper;
import org.hehh.repository.MybatisRepository;
import org.hehh.repository.PageHelperResult;
import org.hehh.repository.domain.Page;
import org.hehh.repository.service.IMybatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author: HeHui
 * @date: 2020-05-31 22:05
 * @description: 通用mapper 数据库 service 实现
 */
public class MybatisServiceImpl<T,ID> implements IMybatisService<T,ID> {


    @Autowired
    private MybatisRepository<T,ID> repository;


    private Class<T> clazz;

    private String idName;


    /**
     *  获取ID名称
     * @return
     */
    private String getIdName(){
        if (null == idName) {
            EntityTable entityTable = EntityHelper.getEntityTable(clazz);
            String[] keyProperties = entityTable.getKeyProperties();
            if (null != keyProperties && keyProperties.length > 0) {
                this.idName = keyProperties[0];
            } else {
                Set<EntityColumn> pkColumns = entityTable.getEntityClassPKColumns();
                if (null != pkColumns && pkColumns.size() > 0) {
                    this.idName = pkColumns.stream().findFirst().get().getProperty();

                }
            }
        }
        return idName;
    }

    /**
     * 手动实现：
     * 谁被实例化那么 字节码对象则为，该实例化对象，所指定（所属）的实体对象
     */
    public MybatisServiceImpl() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        /**
         * 返回表示此类型实际类型参数的 Type 对象的数组
         */
        this.clazz = (Class<T>) pt.getActualTypeArguments()[0];
    }


    /**
     * 获取条件构造器
     *
     * @return
     */
    @Override
    public Example getExample() {
        return Example.builder(clazz).build();
    }


    /**
     * 根据条件查询：
     * 参数：是一个Example条件对象
     *
     * @param example 条件构造器
     * @return {@link List <T>}
     */
    @Override
    public List<T> findListWhere(Example example) {
        return repository.selectByExample(example);
    }



    /**
     * 查询一个
     *
     * @param example 条件构造器
     * @return {@link Optional}
     */
    @Override
    public Optional<T> findOneWhere(Example example) {
        return Optional.ofNullable(repository.selectOneByExample(example));
    }



    /**
     * 根据条件查询总记录数
     *
     * @param example 条件构造器
     * @return
     */
    @Override
    public long findCountWhere(Example example) {
        return repository.selectCountByExample(example);
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
        PageHelper.startPage(page,rows);
        return PageHelperResult.newInstance(repository.selectAll());
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
    public Page<T> findPage(int page, int rows, Example example) {
        PageHelper.startPage(page,rows);
        return PageHelperResult.newInstance(repository.selectByExample(example));
    }

    /**
     * 是否存在ID
     *
     * @param id
     * @return
     */
    @Override
    public boolean existsById(ID id) {
        return repository.existsWithPrimaryKey(id);
    }




    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<T> findAll() {
        return repository.selectAll();
    }

    /**
     * 根据ID查询
     *
     * @param ids
     * @return
     */
    @Override
    public List<T> findInIds(List<ID> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return null;
        }
        /**
         * 通过 当前实例化对象的泛型中的第一个参数的class来创建Example
         */
        Example example = this.getExample();
        /**
         * 创建 条件对象
         */
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn(getIdName(),ids);

        return repository.selectByExample(example);
    }


    /**
     * 根据ID查询单个
     *
     * @param id
     * @return
     */
    @Override
    public Optional<T> findOne(ID id) {
        return Optional.ofNullable(repository.selectByPrimaryKey(id));
    }



    /**
     * 选择性新增
     *
     * @param t
     * @return
     */
    @Override
    public int saveSelective(T t) {
       return repository.insertSelective(t);
    }

    /**
     * 保存记录
     *
     * @param t
     * @return
     */
    @Override
    public int save(T t) {
        return repository.insert(t);
    }



    /**
     * 批量保存（不能忽略null）
     *
     * @param t
     * @return
     */
    @Override
    public int batchSave(List<T> t) {
        return repository.insertList(t);
    }


    /**
     * 根据ID选择性更新 忽略 null
     *
     * @param t 实体
     */
    @Override
    public int updateSelective(T t) {
        return repository.updateByPrimaryKeySelective(t);
    }

    /**
     * 根据ID更改
     *
     * @param t 实体
     * @return
     */
    @Override
    public int update(T t) {
        return repository.updateByPrimaryKey(t);
    }


    /**
     * 根据条件删除
     *
     * @param t 实体
     * @return 影响条数
     */
    @Override
    public int delete(T t) {
        return repository.delete(t);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public int deleteById(ID id) {
        return repository.deleteByPrimaryKey(id);
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
        if(CollectionUtils.isEmpty(ids)){
            return 0;
        }
        /**
         * 通过 当前实例化对象的泛型中的第一个参数的class来创建Example
         */
        Example example = this.getExample();
        /**
         * 创建 条件对象
         */
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn(getIdName(),ids);

        return repository.deleteByExample(example);
    }


    /**
     * 根据条件更改
     *
     * @param t
     * @param example
     * @return
     */
    @Override
    public int updateByWhere(T t, Example example) {
        return repository.updateByExample(t,example);
    }



    /**
     * 根据条件选择性更改
     *
     * @param t
     * @param example
     * @return
     */
    @Override
    public int updateByWhereSelective(T t, Example example) {
        return repository.updateByExampleSelective(t,example);
    }




    /**
     * 根据条件删除
     *
     * @param example
     * @return
     */
    @Override
    public int deleteByWhere(Example example) {
        return repository.deleteByExample(example);
    }
}
