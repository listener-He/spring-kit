package org.hehh.cloud.spring.mvc.tool;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * @author : HeHui
 * @date : 2019-04-24 22:21
 * @describe : json转换工具类
 */
public class ObjectMapperKit {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    static {

        /**
         * Include.Include.ALWAYS 默认
         * Include.NON_DEFAULT 属性为默认值不序列化
         * Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
         * Include.NON_NULL 属性为NULL 不序列化
         */
        // 全部字段序列化
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.registerModule((new SimpleModule()).addSerializer(new NullValueSerializer()));
    }

    /**
     *  对象转json
     * @param obj
     * @return
     */
    public static <T> String toJsonStr(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :  objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     *  byte转实体
     * @param bytes
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T toBean(byte[] bytes,Class<T> tClass ){
        if(null == bytes || null == tClass){
            return null;
        }

        try {
          return   objectMapper.readValue(bytes,tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     *  对象转json忽略null
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String toJsonStrIgnoreNull(T obj){
        if(null == obj){
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     *  转时间
     * @param dateStr
     * @return
     */
    public static Date toDate(String dateStr) throws ParseException{
        if(StrUtil.isBlank(dateStr)){
            return null;
        }
        return  objectMapper.getDateFormat().parse(dateStr);

    }




    /**
     * 对象转json
     *   待格式
     * @param obj
     * @return
     */
    public static <T> String toJsonStrPretty(T obj){
        if(obj == null){
            return null;
        }
        try {
            return  objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 字符串转对象
     * @param str
     * @param clazz
     * @return
     */
    public static <T> T toBean(String str,Class<T> clazz){
        try {
            if(StrUtil.isEmpty(str) || clazz == null){
                return null;
            }

            if(clazz.equals(String.class)){
                return (T)str;
            }
            if(Date.class.isAssignableFrom(clazz)){
                return (T)toDate(str);
            }



            return  objectMapper.readValue(str,clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    /**
     * json字段符转List之类的集合
     * @param str
     * @return
     */
    public static <T> List<T> toList(String str, Class<T> clazz){
        if(StrUtil.isEmpty(str) || clazz == null){
            return null;
        }
        try {
           return objectMapper.readValue(str,getCollectionType(ArrayList.class, clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        if(null == collectionClass || null == elementClasses || ArrayUtil.isEmpty(elementClasses)){
            return null;
        }
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }











    /**
     * 把json解析成list，如果list内部的元素存在jsonString，继续解析
     *
     * @param json
     * @return
     * @throws Exception
     */
    private static List<Object> json2ListRecursion(String json) throws Exception {
        if (json == null) {
            return null;
        }

        List<Object> list = objectMapper.readValue(json, List.class);

        for (Object obj : list) {
            if (obj != null && obj instanceof String) {
                String str = (String) obj;
                if (str.startsWith("[")) {
                    obj = json2ListRecursion(str);
                } else if (obj.toString().startsWith("{")) {
                    obj = json2MapRecursion(str);
                }
            }
        }

        return list;
    }

    /**
     * 把json解析成map，如果map内部的value存在jsonString，继续解析
     *
     * @param json
     * @return
     * @throws Exception
     */
    public static  Map<String,Object> json2MapRecursion(String json) throws Exception {
        if (json == null) {
            return null;
        }

        Map<String, Object> map = objectMapper.readValue(json, Map.class);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            if (obj != null && obj instanceof String) {
                String str = ((String) obj);

                if (str.startsWith("[")) {
                    List<?> list = json2ListRecursion(str);
                    map.put(entry.getKey(), list);
                } else if (str.startsWith("{")) {
                    Map<String, Object> mapRecursion = json2MapRecursion(str);
                    map.put(entry.getKey(), mapRecursion);
                }
            }
        }

        return map;
    }


    public static void main(String[] args) {




    }



}
