package org.hehh.utils.file.office;


import java.util.*;

/**
 * @author: HeHui
 * @date: 2020-08-22 14:34
 * @description: excel监听读取
 */
public interface ExcelListenerReader<T> extends ExcelReader<T> {


    /**
     * 马克斯元素
     */
    static final int max_element = 100000;

    /**
     * 回调方式读取
     *
     * @param callback 回调
     */
    default void readCallback(ListenerCallback<T> callback) {
        this.readCallback(0, callback);
    }


    /**
     * 读回调
     *
     * @param sheetIndex 表索引
     * @param callback   回调
     */
    default void readCallback(int sheetIndex, ListenerCallback<T> callback) {
        this.readCallback(sheetIndex, 0, callback);
    }


    /**
     * 读回调
     *
     * @param sheetIndex 表索引
     * @param startIndex 开始指数
     * @param callback   回调
     */
    void readCallback(int sheetIndex, int startIndex, ListenerCallback<T> callback);


    /**
     * 读取指定工作薄，从指定行开始读取
     *
     * @param sheetIndex 表索引
     * @param startIndex 开始指数
     *
     * @return {@link Optional < Collection <T>>}
     */
    @Override
    default Optional<List<T>> read(int sheetIndex, int startIndex) {

        List<List<T>> results = new ArrayList<>();
        final List<T>[] temp = new List[] {new ArrayList<>(1024)};


        this.readCallback(sheetIndex, startIndex, (data, index) -> {
            if (null != data) {
                temp[0].add(data);
                if (index > 0 && index % max_element == 0) {
                    results.add(temp[0]);
                    temp[0] = new ArrayList<>(1024);
                }
            }
        });

        if (results.isEmpty()) {
            return Optional.empty();
        }

        List<T> result = new ArrayList<>(results.size() * max_element);
        for (List<T> v : results) {
            result.addAll(v);
        }
        results.clear();
        return Optional.of(result);
    }


}
