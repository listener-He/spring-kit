package org.hehh.utils;

import cn.hutool.core.collection.CollUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <br>集合操作类</br>
 *
 * @author: HeHui
 * @date: 2021-03-13 11:45
 */
public class CollectionUtils {


    /**
     * 计算切分次数
     */
    private static int countStep(int size, int max_send) {
        return (size + max_send - 1) / max_send;
    }



    /**
     * 分割集合，流式处理
     *
     * @param collection 集合
     * @param size       大小
     * @return {@link List<List<T>>}
     */
    public static <T> List<List<T>> splitStream(java.util.Collection<T> collection, int size) {
        int limit = countStep(collection.size(), size);
        return Stream.iterate(0, n -> n + 1).limit(limit).parallel().map(a -> collection.stream().skip(a * size).limit(size).parallel().collect(Collectors.toList())).collect(Collectors.toList());
    }

    /**
     * 分割 (效率与上面的没什么区别...，代码还辣么多)
     *
     * @param collection 集合
     * @param size       大小
     * @return {@link List<List<T>>}
     */
    @Deprecated
    public static <T> List<List<T>> split(java.util.List<T> collection, int size) {
        int elementSize = collection.size();
        int toIndex = size;
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < collection.size(); i += size) {
            /**
             * toIndex最后没有size条数据则剩余几条newList中就装几条
             */
            if (i + size > elementSize) {
                toIndex = elementSize - i;
            }
            List<T> newList = collection.subList(i, i + toIndex);
            result.add(newList);
        }
        return result;
    }
}
