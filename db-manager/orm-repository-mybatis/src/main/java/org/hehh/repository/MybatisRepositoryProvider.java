package org.hehh.repository;

import org.apache.ibatis.mapping.MappedStatement;
import org.hehh.repository.util.CollectionUtil;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Optional;
import java.util.Set;

/**
 * @author: HeHui
 * @date: 2020-08-02 18:36
 * @description: 自定义的MybatisRepository 实现
 */
public class MybatisRepositoryProvider  extends MapperTemplate {


    public final static String LIST_START = " <foreach collection=\"list\" item=\"record\" >";
    public final static String LIST_END = " </foreach>";

    public final static String TRIM_SET_START = " <trim prefix=\"set\" suffixOverrides=\",\">";
    public final static String TRIM_SET_END = " </trim>";

    public final static String ITEM = "record";





    /**
     * mybatis库提供者
     *
     * @param mapperClass  mapper类
     * @param mapperHelper 映射器辅助
     */
    public MybatisRepositoryProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }




    public String updateList(MappedStatement ms){
        Class<?> entityClass = getEntityClass(ms);
        return getUpdateListByPk(entityClass,tableName(entityClass),false);

    }




    public String updateListSelective(MappedStatement ms){
        Class<?> entityClass = getEntityClass(ms);
        return getUpdateListByPk(entityClass,tableName(entityClass),true);
    }





    /**
     * 被pk更新列表
     *
     * @param entityClass      实体类型
     * @param notnull notnull
     * @return {@link StringBuilder}
     */
    public static String getUpdateListByPk(Class<?> entityClass,String tableName,boolean notnull) {

        StringBuilder sql = new StringBuilder();
        /**
         *  获取所有列
         */
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        Optional<EntityColumn> optional = EntityHelper.getPKColumns(entityClass).stream().findFirst();
        if(!optional.isPresent()){
            throw  new MapperException("无法获取实体类" + entityClass.getCanonicalName() + "对应的主键!");
        }
        EntityColumn id = optional.get();

        /**
         *  拼接 update table
         */
        sql.append(SqlHelper.updateTable(entityClass, tableName));

        sql.append(TRIM_SET_START);
        for (EntityColumn column : columns) {
            if(!column.isId() && column.isUpdatable()){
                if(notnull){
                    sql.append(" <if test=\"@").append(CollectionUtil.CLASS_NAME).append("@attributeNotNull(").append(ITEM).append(column.getProperty()).append(")\" >");
                }

                sql.append(" <trim prefix=\" ").append(column.getColumn()).append(" =case\" suffix=\"end,\">") ;
                sql.append(LIST_START);
                sql.append(" when ").append(id.getColumnEqualsHolder(ITEM)).append(" then ").append(column.getColumnHolder(ITEM));
                sql.append(LIST_END);
                sql.append(" </trim>") ;

                if(notnull){
                    sql.append(" </if>");
                }

            }
        }

        sql.append(TRIM_SET_END);
        sql.append(" where").append(id.getColumn()).append(" in ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" open=\"(\" close=\")\" >");
        sql.append(id.getColumnHolder(ITEM));
        sql.append("</foreach>");

        return sql.toString();
    }


}
