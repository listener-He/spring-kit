package org.hehh.sitemap.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author: HeHui
 * @date: 2020-08-16 18:35
 * @description: sitemap url链接
 */
@Data
public class SitemapUrl {

    /**
     * 链接
     */
    private final String url;

    /**
     *  更改时间
     */
    private final Date lastMod;
    /**
     *  变更频率
     */
    private final ChangeFreq changeFreq;


    /**
     *  权重
     */
    private final Double priority;




    public SitemapUrl(String url, Date lastMod, ChangeFreq changeFreq, Double priority) {
        this.url = url;
        this.lastMod = lastMod;
        this.changeFreq = changeFreq;
        this.priority = priority;
    }
}
