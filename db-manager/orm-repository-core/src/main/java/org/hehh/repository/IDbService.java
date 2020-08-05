package org.hehh.repository;



import java.util.List;
import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-05-31 20:09
 * @description: db方法接口
 */
public interface IDbService<T,ID> {







    /**
     *  是否存在ID
     * @param id
     * @return
     */
    boolean existsById(ID id);









    /**
     *  查询所有
     * @return
     */
    List<T> findAll();




    /**
     *  根据ID查询
     * @param ids
     * @return
     */
    List<T> findInIds(List<ID> ids);

    /**
     *  根据ID查询单个
     * @param id
     * @return
     */
    Optional<T> findOne(ID id);







    /**
     * 选择性新增
     * @param t
     * @return
     */
    int saveSelective(T t);


    /**
     *  保存记录
     * @param t
     * @return
     */
    int save(T t);


    /**
     *  批量保存（不能忽略null）
     * @param t
     * @return
     */
    int batchSave(List<T> t);


    /**
     * 根据ID选择性更新 忽略 null
     *
     * @param t 实体
     */
    int updateSelective(T t);




    /**
     * 根据ID更改
     *
     * @param t 实体
     * @return
     */
    int update(T t);



    /**
     *  根据条件删除
     * @param t 实体
     * @return 影响条数
     */
    int delete(T t);


    /**
     * 根据id删除
     *
     * @param id
     */
    int deleteById(ID id);

    /**
     * 根据id集合批量删除
     *
     * @param ids id集合
     * @return 删除条数
     */
    int deleteInId(ID... ids);



    /**
     *  批量删除
     * @param ids id集合
     * @return 删除条数
     */
    int deleteInId(List<ID> ids);

}
