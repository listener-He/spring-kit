package org.hehh.repository.util;

import org.springframework.data.util.ReflectionUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author: HeHui
 * @date: 2020-08-02 23:30
 * @description: 集合工具类
 */
public class CollectionUtil {

    public static final String CLASS_NAME = CollectionUtil.class.toString().split(" ")[1];

    /**
     * 属性不是空
     *
     * @param collection    集合
     * @param attributeName 属性名称
     * @return boolean
     */
    public static boolean attributeNotNull(Collection<?> collection,String attributeName){

        if(StringUtils.isEmpty(attributeName)){
            return false;
        }
        if(CollectionUtils.isEmpty(collection)){
            return false;
        }


       return collection.stream().anyMatch(v -> {
            Field field = ReflectionUtils.findField(v.getClass(), x -> {
                if (attributeName.equals(x.getName())) {
                    return true;
                }
                return false;
            });
            if(field != null){
                try {
                    return field.get(v) != null;
                } catch (IllegalAccessException e) {
                }
            }
            return false;
        });
    }


    public static void main(String[] args) {
        System.out.println(CollectionUtil.CLASS_NAME);
    }

}
