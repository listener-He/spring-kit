package org.hehh.sitemap;

/**
 * @author: HeHui
 * @date: 2020-08-16 18:42
 * @description: sitemap爬虫
 */
public interface SitemapCrawl {

    /**
     * 能爬
     *
     * @return boolean
     */
    boolean canCrawl();


    /**
     * 禁用
     */
    void disable();
}
