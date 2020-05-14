package org.hehh.repository.domain;


/**
 * @author : HeHui
 * @date : 2019-02-26 17:51
 * @describe : 分页继承试试
 */
public interface Page<T> extends org.springframework.data.domain.Page<T> {



    /**
     *  下一页
     */
    default Integer nextPage(){
        if(hasNext()){
            return getNumber() + 1;
        }
        return getNumber();
    }






}
