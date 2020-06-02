package org.hehh.cloud.util.http.browser;

import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.OS;

/**
 * @author: HeHui
 * @create: 2020-03-20 22:11
 * @description:  Browser 工具的类型添加
 **/
public class BrowserEnhance {

    static {


        if(ClassLoaderUtil.isPresent("cn.hutool.http.useragent", BrowserEnhance.class.getClassLoader())){

            Browser wacheBrowser = new Browser("WeChat", "MicroMessenger", "MicroMessenger\\/([\\d\\w\\.\\-]+)");
            Browser alipayBrowser = new Browser("Alipay", "Alipay", "alipay\\/([\\d\\w\\.\\-]+)");
            Browser browser360 = new Browser("360", "360SE", "360SE\\/([\\d\\w\\.\\-]+)");
            Browser ucBrowser = new Browser("UCWEB", "UCWEB", "UCWEB\\/([\\d\\w\\.\\-]+)");
            Browser sogouBrowser = new Browser("MetaSr", "MetaSr", "MetaSr\\/([\\d\\w\\.\\-]+)");
            Browser ttBrowser = new Browser("TencentTraveler", "TencentTraveler", "TencentTraveler\\/([\\d\\w\\.\\-]+)");

            OS iosOs = new OS("iPhone", "ios");
            OS.oses.add(iosOs);


            Browser.browers.add(wacheBrowser);
            Browser.browers.add(alipayBrowser);
            Browser.browers.add(browser360);
            Browser.browers.add(ucBrowser);
            Browser.browers.add(sogouBrowser);
            Browser.browers.add(ttBrowser);
        }


    }
}
