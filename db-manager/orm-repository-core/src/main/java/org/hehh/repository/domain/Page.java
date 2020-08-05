package org.hehh.repository.domain;


import org.hehh.cloud.common.bean.result.PageResult;

/**
 * @author : HeHui
 * @date : 2019-02-26 17:51
 * @describe : 分页继承试试
 */
public interface Page<T> extends org.springframework.data.domain.Page<T> {


    /**
     *  转响应类
     * @return
     */
    PageResult<T> toResult();



    /**
     *  下一页
     */
    default int nextPage(){
        if(hasNext()){
            return getNumber() + 1;
        }
        return getNumber();
    }



}
