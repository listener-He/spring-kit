package org.hehh.repository.domain;

import org.hehh.cloud.common.bean.result.PageResult;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-08-04 10:34
 * @description: jpa分页返回
 */
public class JpaPageResult<T> extends PageImpl<T> implements Page<T> {


    /**
     * Constructor of {@code JpaPageResult}.
     *
     * @param content  the content of this page, must not be {@literal null}.
     * @param pageable the paging information, must not be {@literal null}.
     * @param total    the total amount of items available. The total might be adapted considering the length of the content
     */
    public JpaPageResult(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }


    /**
     * Constructor of {@code JpaPageResult}.
     *
     */
    public JpaPageResult(org.springframework.data.domain.Page<T> page){
        this(page.getContent(),page.getPageable(),page.getTotalElements());
    }



    public static <T> JpaPageResult<T> create(org.springframework.data.domain.Page<T> page){
        return new JpaPageResult<>(page);
    }




    /**
     * 转响应类
     *
     * @return
     */
    @Override
    public PageResult<T> toResult() {
        return null;
    }

    /**
     * 下一页
     */
    @Override
    public int nextPage() {
        return 0;
    }
}
