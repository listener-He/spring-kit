package org.hehh.utils.bean;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import javax.xml.crypto.Data;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author: HeHui
 * @create: 2020-03-15 20:58
 * @description: 类操作
 **/
public class BeanKit {




    /**
     * 实体转map
     * @param obj 任何类（该传什么自己心里有数!）
     * @return
     * @throws Exception
     */
    public static Map<String,Object> toMap(Object obj) {
        Map<String,Object> map=new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            Object o = null;
            try {
                o = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if(null != o && !"null".equals(o.toString())){
                map.put(field.getName(), o);
            }

        }
        return map;
    }


    /**
     *  map转实体
     * @param map map对象
     * @param clz 转成的类型
     * @return T bean对象
     * @throws Exception
     */
    public static <T>T toBean(Map<String,Object> map,Class<T> clz) throws IllegalAccessException{
        if(MapUtil.isEmpty(map)){
            return null;
        }

        T obj = null;

        try {
            obj = clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        if(null != obj){
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for(Field field:declaredFields){
                int mod = field.getModifiers();
                if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        }

        return obj;
    }






    /**
     *  判断bean是否为null
     * @param bean 任何类
     * @return
     */
    public static boolean isNotNull(Object bean){
        if(null == bean){
            return false;
        }

        if(bean instanceof String){
            return StrUtil.isNotBlank(bean.toString());
        }
        if(bean instanceof Number){
            return null != bean;
        }
        if(bean instanceof Data){
            return null != bean;
        }

        return false;
    }





    /**
     *  对象是否为null
     *          所有属性为null也视为null
     * @param bean 任何类
     * @return 是否
     */
    public static boolean isNull(Object bean){
        return !isNotNull(bean);
    }


    /**
     *  判断有字段不为null
     * @param obj 任何类
     * @return 是否
     */
    public static boolean fieldIsNotNull(Object obj){
        if(isNull(obj)){
            return false;
        }


        /**
         *  反射得到所有字段
         */
        Object[] fieldsValue = ReflectUtil.getFieldsValue(obj);

        if(ArrayUtil.isEmpty(fieldsValue)){
            return false;
        }

        /**
         *  一个字段不为null就判定对象字段不为null
         */
        for (Object value : fieldsValue) {
            if(null != value){
                return true;
            }
        }


        return false;
    }





    /**
     *    克隆方式复制list
     * @param object 原集合
     * @param clazz 元素类型class
     * @param <T> 元素类型
     * @return
     */
    public static <T> List<T> copyList(Collection<?> object, Class<T> clazz){
        if(CollUtil.isEmpty(object)){
            return null;
        }
        return (List<T> )Convert.toCollection(ArrayList.class, clazz, object);
    }







    /**
     *   克隆方式复制list
     * @param object 原集合类
     * @param className 类全名
     * @param <T>
     * @return
     */
    public static <T>List<T> copyList(Collection<?> object,String className){
        return  copyList(object, ClassUtil.loadClass(className,false));
    }









    /**
     *   copy对象 浅复制（地址值不变）
     * @param object 原对象
     * @param clazz 目标类型
     * @param <T>
     * @return
     */
    public static <T> T copy(Object object,Class<T> clazz){
        if(null == object){
            return null;
        }
        return Convert.convert(clazz,object);
    }

    /**
     *   根据class字符串转copy 浅复制（地址值不变）
     * @param object 原类
     * @param classStr 目录类型字符串
     * @return
     */
    public static <T> T copy(Object object,String classStr){
        return  copy(object, ClassUtil.loadClass(classStr,false));
    }


    /**
     * 深拷贝
     *
     * @param object 对象
     * @param tClass t类
     * @return {@link T}
     */
    public static <T> T deepCopy(Object object,Class<T> tClass){
        if(object == null){
            return null;
        }
        assert tClass != null : "class not null";

        try {
            /**
             * 序列化
             */
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(object);

            /**
             *  反序列化
             */
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return copy(ois.readObject(),tClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }


}
