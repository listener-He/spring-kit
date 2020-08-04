package org.hehh.repository.support;

import com.sun.istack.Nullable;
import org.hehh.repository.DbJpaRepositoryImpl;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Special adapter for Springs {@link org.springframework.beans.factory.FactoryBean} interface to allow easy setup of
 *   repository factories via Spring configuration.
 * @author: HeHui
 * @date: 2020-08-04 10:47
 * @description:
 */
public class DbJpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID>
    extends TransactionalRepositoryFactoryBeanSupport<T, S, ID> {


    private @Nullable EntityManager entityManager;
    private EntityPathResolver entityPathResolver;
    private EscapeCharacter escapeCharacter = EscapeCharacter.DEFAULT;
    private JpaQueryMethodFactory queryMethodFactory;


    /**
     * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public DbJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    /**
     * The {@link EntityManager} to be used.
     *
     * @param entityManager the entityManager to set
     */
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport#setMappingContext(org.springframework.data.mapping.context.MappingContext)
     */
    @Override
    public void setMappingContext(MappingContext<?, ?> mappingContext) {
        super.setMappingContext(mappingContext);
    }

    /**
     * Configures the {@link EntityPathResolver} to be used. Will expect a canonical bean to be present but fallback to
     * {@link SimpleEntityPathResolver#INSTANCE} in case none is available.
     *
     * @param resolver must not be {@literal null}.
     */
    @Autowired
    public void setEntityPathResolver(ObjectProvider<EntityPathResolver> resolver) {
        this.entityPathResolver = resolver.getIfAvailable(() -> SimpleEntityPathResolver.INSTANCE);
    }

    /**
     * Configures the {@link JpaQueryMethodFactory} to be used. Will expect a canonical bean to be present but will
     * fallback to {@link org.springframework.data.jpa.repository.query.DefaultJpaQueryMethodFactory} in case none is
     * available.
     *
     * @param factory may be {@literal null}.
     */
    @Autowired
    public void setQueryMethodFactory(@Nullable JpaQueryMethodFactory factory) {

        if (factory != null) {
            this.queryMethodFactory = factory;
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport#doCreateRepositoryFactory()
     */
    @Override
    protected RepositoryFactorySupport doCreateRepositoryFactory() {

        Assert.state(entityManager != null, "EntityManager must not be null!");

        return createRepositoryFactory(entityManager);
    }

    /**
     * Returns a {@link RepositoryFactorySupport}.
     */
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {

        //TODO 此处 替换 存储库工厂
        JpaRepositoryFactory jpaRepositoryFactory = new DbJpaRepositoryFactory(entityManager);
        jpaRepositoryFactory.setEntityPathResolver(entityPathResolver);
        jpaRepositoryFactory.setEscapeCharacter(escapeCharacter);

        if (queryMethodFactory != null) {
            jpaRepositoryFactory.setQueryMethodFactory(queryMethodFactory);
        }

        return jpaRepositoryFactory;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {

        Assert.state(entityManager != null, "EntityManager must not be null!");

        super.afterPropertiesSet();
    }

    public void setEscapeCharacter(char escapeCharacter) {

        this.escapeCharacter = EscapeCharacter.of(escapeCharacter);
    }







    /**
     *  jpaRepository 工厂
     */
    public static class DbJpaRepositoryFactory extends JpaRepositoryFactory {



        /**
         * Creates a new {@link JpaRepositoryFactory}.
         *
         * @param entityManager must not be {@literal null}
         */
        public DbJpaRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
        }


        /**
         *  定义返回类型
         * @param metadata
         * @return
         */
        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return DbJpaRepositoryImpl.class;
        }
    }

}
