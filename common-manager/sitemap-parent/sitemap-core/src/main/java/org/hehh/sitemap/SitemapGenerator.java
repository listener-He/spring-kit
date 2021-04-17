package org.hehh.sitemap;

import org.hehh.sitemap.bean.SitemapUrl;

import java.net.MalformedURLException;
import java.util.Collection;

/**
 * @author: HeHui
 * @date: 2020-08-16 18:34
 * @description: sitemap 生成器
 */
public interface SitemapGenerator {


    /**
     * 把网站地图
     *
     * @param urls            url
     * @param sitemapFileName 站点地图文件名称
     * @throws MalformedURLException 畸形urlexception
     */
    void putSitemap(Collection<SitemapUrl> urls,String sitemapFileName) throws MalformedURLException;


    /**
     *  生成文件
     */
    void generate();


}
