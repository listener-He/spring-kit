package org.hehh.repository;

import com.github.pagehelper.PageInfo;
import org.hehh.cloud.common.bean.result.PageResult;
import org.hehh.repository.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-05-31 22:15
 * @description: 分页助手 返回类
 */
public class PageHelperResult<T> implements Page<T> {


    /**
     *  当前页
     */
    private final int pageNumber;

    /**
     *  页大小
     */
    private final int pageSize;

    /**
     *  总记录数
     */
    private final int pageTotal;

    /**
     * 总页数
     */
    private final int pageCount;


    /**
     *  分页内容
     */
    private final List<T> content;

    /**
     *  是否有下一页
     */
    private final Boolean lastPage;


    /**
     *  下一页
     */
    private final int nextPage;


    /**
     *  分页 （没什么用的）
     */
    private Pageable pageable;

    public PageHelperResult(int pageNumber, int pageSize, int pageTotal, int pageCount, List<T> content, Boolean lastPage, int nextPage) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.pageTotal = pageTotal;
        this.pageCount = pageCount;
        this.content = content;
        this.lastPage = lastPage;
        this.nextPage = nextPage;
        this.pageable =  PageRequest.of(pageNumber,pageSize);
    }


    /**
     *  创建分页助手返回对象
     * @param content 使用分页助手后返回的list实际应该是 {@link com.github.pagehelper.Page<T> 这个类型}
     *               通过 {@link PageInfo#of(List)} 得到返回数据
     * @param <T> 元素
     * @return
     */
    public static <T> PageHelperResult<T> newInstance(List<T> content){

        if(!CollectionUtils.isEmpty(content)){
            PageInfo<T> pageInfo =  PageInfo.of(content);
            PageHelperResult result = new PageHelperResult(pageInfo.getPageNum(),pageInfo.getPageSize(),
                    Long.valueOf(pageInfo.getTotal()).intValue(),pageInfo.getPages(),pageInfo.getList(),pageInfo.isIsLastPage(),pageInfo.getNextPage());
            return result;
        }

        return new PageHelperResult(0,0,0,0,null,false,0);
    }


    /**
     * 转响应类
     *
     * @return
     */
    @Override
    public PageResult<T> toResult() {
        return PageResult.create(content,pageTotal,pageCount,pageNumber);
    }

    /**
     * Returns the number of total pages.
     *
     * @return the number of total pages
     */
    @Override
    public int getTotalPages() {
        return pageCount;
    }

    /**
     * Returns the total amount of elements.
     *
     * @return the total amount of elements
     */
    @Override
    public long getTotalElements() {
        return pageTotal;
    }



    /**
     * Returns a new {@link Page} with the content of the current one mapped by the given {@link Function}.
     *
     * @param converter must not be {@literal null}.
     * @return a new {@link Page} with the content of the current one mapped by the given {@link Function}.
     * @since 1.10
     */
    @Override
    public <U> org.springframework.data.domain.Page<U> map(Function<? super T, ? extends U> converter) {
        return PageHelperResult.newInstance(getConvertedContent(converter));
    }

    /**
     * Returns the number of the current {@link Slice}. Is always non-negative.
     *
     * @return the number of the current {@link Slice}.
     */
    @Override
    public int getNumber() {
        return pageNumber;
    }

    /**
     * Returns the size of the {@link Slice}.
     *
     * @return the size of the {@link Slice}.
     */
    @Override
    public int getSize() {
        return pageSize;
    }

    /**
     * Returns the number of elements currently on this {@link Slice}.
     *
     * @return the number of elements currently on this {@link Slice}.
     */
    @Override
    public int getNumberOfElements() {
        return content == null ? 0 : content.size();
    }

    /**
     * Returns the page content as {@link List}.
     *
     * @return
     */
    @Override
    public List<T> getContent() {
        return content;
    }

    /**
     * Returns whether the {@link Slice} has content at all.
     *
     * @return
     */
    @Override
    public boolean hasContent() {
        return !CollectionUtils.isEmpty(content);
    }



    /**
     * Returns the sorting parameters for the {@link Slice}.
     *
     * @return
     */
    @Override
    public Sort getSort() {
        return pageable.getSort();
    }

    /**
     * Returns whether the current {@link Slice} is the first one.
     *
     * @return
     */
    @Override
    public boolean isFirst() {
        return pageNumber <= 1;
    }

    /**
     * Returns whether the current {@link Slice} is the last one.
     *
     * @return
     */
    @Override
    public boolean isLast() {
        return !hasNext();
    }

    /**
     * Returns if there is a next {@link Slice}.
     *
     * @return if there is a next {@link Slice}.
     */
    @Override
    public boolean hasNext() {
        return !lastPage;
    }

    /**
     * Returns if there is a previous {@link Slice}.
     *
     * @return if there is a previous {@link Slice}.
     */
    @Override
    public boolean hasPrevious() {
        return pageNumber > 0;
    }

    /**
     * Returns the {@link Pageable} to request the next {@link Slice}. Can be {@link Pageable#unpaged()} in case the
     * current {@link Slice} is already the last one. Clients should check {@link #hasNext()} before calling this method.
     *
     * @return
     * @see #nextOrLastPageable()
     */
    @Override
    public Pageable nextPageable() {
        return hasNext() ? pageable.next() : Pageable.unpaged();
    }



    /**
     * Returns the {@link Pageable} to request the previous {@link Slice}. Can be {@link Pageable#unpaged()} in case the
     * current {@link Slice} is already the first one. Clients should check {@link #hasPrevious()} before calling this
     * method.
     *
     * @return
     * @see #previousPageable()
     */
    @Override
    public Pageable previousPageable() {
        return hasPrevious() ? pageable.previousOrFirst() : Pageable.unpaged();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }





    /**
     * Applies the given {@link Function} to the content of the
     *
     * @param converter must not be {@literal null}.
     * @return
     */
    protected <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {

        Assert.notNull(converter, "Function must not be null!");

        return this.stream().map(converter::apply).collect(Collectors.toList());
    }
}
