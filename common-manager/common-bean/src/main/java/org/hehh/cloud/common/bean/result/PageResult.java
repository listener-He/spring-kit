package org.hehh.cloud.common.bean.result;

import lombok.Data;

import java.util.List;

/**
 * @author : HeHui
 * @date : 2019-02-26 10:23
 * @describe : 分页返回类
 */
@Data
public class PageResult<T> extends Result<List<T>> {



    /**
     *  总记录数
     */
    private Long pageTotal;

    /**
     * 总页数
     */
    private Long pageCount;


    private Integer pageNumber;

    /**
     * 是否有下一页
     */
    private Boolean next;




    /**
     *   创建分页对象
     * @param <T> 数据内容类型
     * @param content 页数据
     * @param pageTotal 总记录数
     * @param pageCount  总页数
     * @return
     */
    public static <T> PageResult<T> create(List<T> content, long pageTotal,long pageCount) {
        return create(
                content,pageTotal,pageCount,null);
    }


    /**
     *  创建分页对象
     * @param content
     * @param pageTotal
     * @param pageCount
     * @param pageNumber
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> create(List<T> content, long pageTotal,long pageCount,Integer pageNumber) {
        PageResult<T> page = new PageResult<>();
        page.setPageTotal(pageTotal);
        page.setPageCount(pageCount);
        page.setPageNumber(pageNumber);
        page.setCode(0);
        if (content != null && !content.isEmpty()) {
            page.setData(content);
        }
        return page;
    }
}
