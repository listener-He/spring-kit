package org.hehh.repository.domain;

import lombok.Setter;

/**
 * @author: HeHui
 * @date: 2020-07-09 14:29
 * @description: 分页参数
 */
public class PageDto {

    //当前页
    @Setter
    private Integer pageNumber;

    //页大小
    @Setter
    private Integer  pageSize;


    //当前页简称
    @Setter
    private Integer pn;

    //页大小简称
    @Setter
    private Integer ps;


    public int getPageNumber(){
        if(null == pageNumber && null != pn){pageNumber = pn;}
        if(null == pageNumber || pageNumber < 1){
            return 1;
        }
        return pageNumber;
    }

    public int getPageSize(){
        if(null == pageSize && null != ps){pageSize = ps;}
        if(null == pageSize || pageSize < 1){
            return 10;
        }
        if(this.pageSize > 1000){
            return 1000;
        }
        return this.pageSize;
    }

}
