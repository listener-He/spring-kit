package org.hehh.repository;

import org.hehh.repository.domain.CriterionSpecification;
import org.hehh.repository.domain.Example;
import org.hehh.repository.domain.JpaPageResult;
import org.hehh.repository.domain.Page;
import org.hibernate.query.criteria.internal.CriteriaQueryImpl;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.SingularAttribute;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-08-03 00:41
 * @description: 自定义jap Repository实现
 */
public class DbJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements DbJpaRepository<T,ID> {

    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";


    public static final String INSERT_QUERY_STRING = "insert into from %s ";

    public static final String UPDATE_QUERY_STRING = "update %s ";


    private final JpaEntityInformation<T, ?> entityInformation;

    private final EntityManager entityManager;

    private final Set<SingularAttribute<? super T, ?>> attributes;


    /**
     * Creates a new {@link SimpleJpaRepository} to manage objects of the given {@link JpaEntityInformation}.
     *
     * @param entityInformation must not be {@literal null}.
     * @param entityManager     must not be {@literal null}.
     */
    public DbJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;

        IdentifiableType<T> type = (IdentifiableType<T>) entityManager.getMetamodel().managedType(entityInformation.getJavaType());
        this.attributes = type.getSingularAttributes();
    }


    /**
     * Creates a new {@link SimpleJpaRepository} to manage objects of the given domain type.
     *
     * @param domainClass must not be {@literal null}.
     * @param em          must not be {@literal null}.
     */
    public DbJpaRepositoryImpl(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }



    /**
     * 根据id删除
     *
     * @param ids
     */
    @Transactional
    @Override
    public int deleteByIdIn(List<ID> ids) {
        Assert.notEmpty(ids, ID_MUST_NOT_BE_NULL);

        SingularAttribute<? super T, ?> idAttribute = entityInformation.getIdAttribute();
        if(idAttribute == null){
            throw new IllegalArgumentException(
                String.format("Could not obtain required identifier attribute for table %s!", entityInformation.getEntityName()));
        }
        return applyAndBind(QueryUtils.getQueryString(QueryUtils.DELETE_ALL_QUERY_STRING, entityInformation.getEntityName()),idAttribute,ids,entityManager)
                        .executeUpdate();
    }


    /**
     * 根据主键更新
     *
     * @param entity 实体
     * @return int
     */
    @Transactional
    @Override
    public <S extends T> int update(S entity) {
        Assert.notEmpty(attributes,"entity nothing needs to be changed attribute");
        return applyUpdate(entity,false).executeUpdate();
    }



    /**
     * 根据条件更新
     *
     * @param entity  实体
     * @param example 条件表达式
     * @return int
     */
    @Override
    public <S extends T> int update(S entity, Example<T> example) {
        return applyUpdateExample(entity, example,false);
    }



    /**
     * 选择性更新，忽略null。根据主键更新
     *
     * @param entity 实体
     * @return int
     */
    @Transactional
    @Override
    public <S extends T> int updateSelective(S entity) {
        Assert.notEmpty(attributes,"entity nothing needs to be changed attribute");
        return applyUpdate(entity,true).executeUpdate();
    }



    /**
     * 根据条件选择性更新
     *
     * @param entity  实体
     * @param example
     * @return int
     */
    @Transactional
    @Override
    public <S extends T> int updateSelective(S entity, Example<T> example) {
        return applyUpdateExample(entity, example,true);
    }


    /**
     * 应用更新的例子
     *
     * @param entity  实体
     * @param example 例子
     * @param notNull 非空
     * @return int
     */
    private <S extends T> int applyUpdateExample(S entity, Example<T> example,boolean notNull) {
        Assert.notNull(entity,"entity not null");
        Assert.notNull(example,"Example not null");

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaUpdate<?> update = builder.createCriteriaUpdate(entity.getClass());

        /**
         * 获取Bean
         */
        BeanWrapper srcBean = new BeanWrapperImpl(entity);
        PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
        Assert.notEmpty(pds,"Entity property is not null");


        for (PropertyDescriptor pd : pds) {

            if(notNull){
                if(!StringUtils.isEmpty(srcBean.getPropertyValue(pd.getName()))){
                    update.set(pd.getName(),srcBean.getPropertyValue(pd.getName()));
                }
            }else{
                update.set(pd.getName(),srcBean.getPropertyValue(pd.getName()));
            }
        }


        update.where(example.getCriterion().toPredicate((Root<T>) update.getRoot(),null,builder));
        return entityManager.createQuery(update).executeUpdate();
    }


    /**
     * 批量更新
     *
     * @param recordList 记录列表
     * @return int
     */
    @Transactional
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
    @Transactional
    @Override
    public int updateListSelective(List<T> recordList) {
        return 0;
    }




    /**
     * 找到一个
     *
     * @param example 例子
     * @return {@link Optional<T>}
     */
    @Override
    public Optional<T> findOne(Example<T> example) {
       return super.findOne(new CriterionSpecification<>(example.getCriterion()));
    }

    /**
     * 找到多个
     *
     * @param example 例子
     * @return {@link Optional<T>}
     */
    @Override
    public List<T> findAll(Example<T> example) {
        return super.findAll(new CriterionSpecification<>(example.getCriterion()),example.getSort());
    }

    /**
     * 分页
     *
     * @param example
     * @return
     */
    @Override
    public Page<T> findPage(Example<T> example) {

        if(example.getCriterion() == null){
            return JpaPageResult.create(super.findAll(example.getPageable()));
        }
        return JpaPageResult.create(super.findAll(new CriterionSpecification<>(example.getCriterion()),example.getPageable()));
    }





    /**
     *  apply 更新
     *
     * @param entity must not be {@literal null}.
     * @param notNull is ignore null attribute
     * @return Guaranteed to be not {@literal null}.
     */
    public Query applyUpdate(T entity, boolean notNull){
        Assert.notNull(entity, "entity must not be null!");

        SingularAttribute<? super T, ?> idAttribute = entityInformation.getIdAttribute();
        ID id = (ID)entityInformation.getId(entity);

        Assert.notNull(id, "entity id not null !");




        Set<SingularAttribute<? super T, ?>> updateAttribute = new HashSet<>();
        updateAttribute.addAll(attributes.stream().filter(v-> !v.isId()).collect(Collectors.toSet()));

        /**
         *  忽略空值
         */
        if(notNull){
            getNullProperties(entity).ifPresent(v-> {
                updateAttribute.removeAll(attributes.stream().filter(a -> !v.contains(a.getJavaMember().getName())).collect(Collectors.toSet()));
            });
        }

        if(updateAttribute.isEmpty()){
            throw new IllegalArgumentException("entity nothing needs to be changed attribute");
        }


        StringBuilder builder = new StringBuilder(QueryUtils.getQueryString(UPDATE_QUERY_STRING,entityInformation.getEntityName()));
        builder.append(" set ");

        int i = 0;

        Iterator<SingularAttribute<? super T, ?>> iterator = updateAttribute.iterator();

        /**
         *  拼接 set 语句
         */
        while (iterator.hasNext()){
            SingularAttribute<? super T, ?> attribute = iterator.next();
            builder.append(String.format(" %s = ?%d", attribute.getName(), ++i));

            if (iterator.hasNext()) {
                builder.append(" ,");
            }
        }


        builder.append(" where ").append(String.format(" %s = ?%d", idAttribute.getName(), ++i));

        /**
         *  创建语句
         */
        Query query = entityManager.createQuery(builder.toString());

        /**
         *  重置索引
         */
        i = 0;


        BeanWrapper srcBean = new BeanWrapperImpl(entity);
        while (iterator.hasNext()) {
            SingularAttribute<? super T, ?> attribute = iterator.next();
            String name = attribute.getJavaMember().getName();
            query.setParameter(++i,srcBean.getPropertyValue(name) );
        }
        query.setParameter(++i,id);

        return query;

    }



    /**
     * Creates a where-clause referencing the given entities key and appends it to the given query string. Binds the given
     * entities to the query.
     *
     * @param <ID> type of the entities key.
     * @param queryString must not be {@literal null}.
     * @param idAttribute  entities key
     * @param entities must not be {@literal null}.
     * @param entityManager must not be {@literal null}.
     * @return Guaranteed to be not {@literal null}.
     */
    public static <ID,T> Query applyAndBind(String queryString, SingularAttribute<? super T, ?> idAttribute,Iterable<ID> entities, EntityManager entityManager) {

        Assert.notNull(queryString, "Querystring must not be null!");
        Assert.notNull(entities, "Iterable of entities must not be null!");
        Assert.notNull(entityManager, "EntityManager must not be null!");
        Assert.notNull(idAttribute, "SingularAttribute must not be null!");

        Iterator<ID> iterator = entities.iterator();

        if (!iterator.hasNext()) {
            throw new IllegalArgumentException("entities must not be null");
        }

        
        StringBuilder builder = new StringBuilder(queryString);
        
        builder.append(" where ").append(idAttribute.getName()).append(" in (");

        int i = 0;

        while (iterator.hasNext()) {

            iterator.next();

            builder.append(String.format(" ?%d", ++i));

            if (iterator.hasNext()) {
                builder.append(" ,");
            }
        }

        builder.append(")");

        Query query = entityManager.createQuery(builder.toString());

        iterator = entities.iterator();
        i = 0;

        while (iterator.hasNext()) {
            query.setParameter(++i, iterator.next());
        }

        return query;
    }




    /**
     * 获取空属性
     *
     * @param src src
     * @return {@link String[]}
     */
    public static Optional<List<String>> getNullProperties(Object src) {
        /**
         * 获取Bean
         */
        BeanWrapper srcBean = new BeanWrapperImpl(src);
        /**
         * 获取Bean的属性描述
         */
        PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
        /**
         * 获取Bean的空属性
         */
        List<String> properties = new LinkedList<>();
        for (PropertyDescriptor propertyDescriptor : pds) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = srcBean.getPropertyValue(propertyName);
            if (StringUtils.isEmpty(propertyValue)) {
                srcBean.setPropertyValue(propertyName, null);
                properties.add(propertyName);
            }
        }
        if(properties.size() == 0){
            return Optional.empty();
        }
        return Optional.of(properties);
    }

}
