package org.hehh.cloud.spring.userAgent;

import cn.hutool.http.useragent.UserAgentUtil;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-08-10 01:14
 * @description: hutool user-agent 处理器
 */
public class HuToolUserAgentProcessor implements UserAgentProcessor {


    public HuToolUserAgentProcessor(List<UserAgentConfiguration> configurations){
        if (!CollectionUtils.isEmpty(configurations)) {
            List<Browser> browsers = new LinkedList<>();
            List<OS> osList = new LinkedList<>();
            List<Platform> platforms = new LinkedList<>();

            for (UserAgentConfiguration configuration : configurations) {
                configuration.addBrowser(browsers);
                configuration.addOS(osList);
                configuration.addPlatform(platforms);
            }

            browser(browsers);
            os(osList);
            platform(platforms);
        }
    }


    private void browser(List<Browser> browsers) {

        for (Browser v : browsers) {

            cn.hutool.http.useragent.Browser wacheBrowser = new cn.hutool.http.useragent.Browser(v.getName(), v.getRegex(), v.getVersionRegex());
            cn.hutool.http.useragent.Browser.browers.add(wacheBrowser);
        }

    }


    private void os(List<OS> osList) {
        for (OS v : osList) {
            cn.hutool.http.useragent.OS os = new cn.hutool.http.useragent.OS(v.getName(), v.getRegex());
            cn.hutool.http.useragent.OS.oses.add(os);
        }
    }

    private void platform(List<Platform> platforms){
        for (Platform v : platforms) {
            cn.hutool.http.useragent.Platform platform = new cn.hutool.http.useragent.Platform(v.getName(), v.getRegex());
            cn.hutool.http.useragent.Platform.platforms.add(platform);
        }
    }

    /**
     * 解析
     *
     * @param userAgentStr 用户代理str
     * @return {@link UserAgent}
     */
    @Override
    public UserAgent parse(String userAgentStr) {
        cn.hutool.http.useragent.UserAgent parse = UserAgentUtil.parse(userAgentStr);
        UserAgent userAgent = new UserAgent();
        userAgent.setMobile(parse.isMobile());
        if(parse.isMobile()){
            userAgent.setMobileSystem(parse.getPlatform().getName());
        }
        userAgent.setBrowserName(parse.getBrowser().getName());
        userAgent.setOsName(parse.getOs().getName());
        return userAgent;
    }
}
