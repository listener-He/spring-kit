package org.hehh.sitemap;

import com.redfin.sitemapgenerator.*;
import org.hehh.sitemap.bean.SitemapUrl;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-08-16 22:36
 * @description: Sitemap 和 index 生成组合 （web版）
 */
public class WebSitemapAndIndexGenerator implements SitemapGenerator {

    /**
     *  Sitemap基础目录
     */
    private final File basisDirectory;

    /**
     * 基础域名
     */
    private final String basisDomain;


    private final SitemapIndexGenerator sitemapIndexGenerator;

    private int maxUrl = 30000;





    /**
     *  用于构建web Sitemap 生成器 基于 SitemapIndexGenerator 和 WebSitemapGenerator实现
     *
     * @param basisDirectory Sitemap文件基础存放目录
     * @param basisDomain sitemap-index 访问域名
     * @param indexFileName sitemap-index 文件名称
     * @throws MalformedURLException
     */
    public WebSitemapAndIndexGenerator(String basisDirectory,String basisDomain,String indexFileName) throws MalformedURLException {
        this(new File(basisDirectory),basisDomain,indexFileName);
    }


    /**
     *  用于构建web Sitemap 生成器 基于 SitemapIndexGenerator 和 WebSitemapGenerator实现
     *
     * @param basisDirectory Sitemap文件基础存放目录
     * @param basisDomain sitemap-index 访问域名
     * @param indexFileName sitemap-index 文件名称
     * @throws MalformedURLException
     */
    public WebSitemapAndIndexGenerator(File basisDirectory,String basisDomain,String indexFileName) throws MalformedURLException {
        assert basisDirectory != null : "Sitemap文件基础目录不能为空";
        if (!basisDirectory.exists()) {
            basisDirectory.mkdirs();
        }
        assert basisDomain != null : "Sitemap-Index域名不能为空";
        assert indexFileName != null : "Sitemap-Index文件名不能为空";

        this.basisDirectory = basisDirectory;
        this.basisDomain = basisDomain;
        /**
         * 构造 sitemap_index 生成器
         */
        W3CDateFormat dateFormat = new W3CDateFormat(W3CDateFormat.Pattern.DAY);

        sitemapIndexGenerator = new SitemapIndexGenerator
            .Options(basisDomain, new File( this.basisDirectory, indexFileName))
            .dateFormat(dateFormat)
            .autoValidate(true)
            .build();

    }


    /**
     * 把网站地图
     *
     * @param urls            url
     * @param sitemapFileName 站点地图文件名称
     * @throws MalformedURLException 畸形urlexception
     */
    @Override
    public void putSitemap(Collection<SitemapUrl> urls, String sitemapFileName) throws MalformedURLException {
        assert sitemapFileName != null : "文件前缀明不能为空";

        /**
         *  构建SitemapGenerator 用于生成地图
         */
        WebSitemapGenerator webSitemapGenerator = WebSitemapGenerator.builder(basisDomain, basisDirectory)
            .fileNamePrefix(sitemapFileName).dateFormat(new W3CDateFormat(W3CDateFormat.Pattern.DAY)).maxUrls(maxUrl).build();


        /**
         *  添加url
         */
        for (SitemapUrl v : urls) {
            if(v != null){
                webSitemapGenerator.addUrl(new WebSitemapUrl.Options(v.getUrl()).changeFreq(ChangeFreq.valueOf(v.getChangeFreq().toString())).priority(v.getPriority()).lastMod(v.getLastMod()).build());
            }
        }



        /**
         *  生成siteMap文件
         */
        List<File> siteMapFiles = webSitemapGenerator.write();

        for (File f : siteMapFiles) {
            sitemapIndexGenerator.addUrl(this.basisDomain+ "/" + f.getName());
        }
    }


    /**
     * 生成文件
     */
    @Override
    public void generate() {
        sitemapIndexGenerator.write();
    }
}
