package org.hehh.utils.file.excel;

import java.util.List;
import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-08-22 14:27
 * @description: excel读取器
 */
public interface ExcelReader<T> {


    /**
     * 读取，默认读取第一个工作薄，从第一行开始读取
     *
     * @return {@link Optional<List<T>>}
     */
    default Optional<List<T>> read(){
        return this.read(0);
    }


    /**
     * 读取指定工作薄，从第一行开始读取
     *
     * @param sheetIndex 表索引
     * @return {@link Optional<List<T>>}
     */
    default Optional<List<T>> read(int sheetIndex){
        return this.read(sheetIndex,0);
    }



    /**
     * 读取指定工作薄，从指定行开始读取
     *
     * @param sheetIndex 表索引
     * @param startIndex 开始指数
     * @return {@link Optional<List<T>>}
     */
    Optional<List<T>> read(int sheetIndex,int startIndex);



}
