package org.hehh.cloud.spring.mvc.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: HeHui
 * @date: 2020-07-26 17:37
 * @description: 路由匹配的请求一次过滤器
 */
@Slf4j
public abstract class AbstractPathOncePerRequestFilter extends OncePerRequestFilter {


    protected final AntPathMatcher antPathMatcher;

    private final UrlPathHelper urlPathHelper ;

    /**
     * Exclude url patterns.
     */
    private final Set<String> excludeUrlPatterns = new HashSet<>();

    private final Set<String> urlPatterns = new LinkedHashSet<>();

    /**
     * 每个请求路径一旦过滤器
     */
    public AbstractPathOncePerRequestFilter(){
        this(new AntPathMatcher(),new UrlPathHelper());
    }



    /**
     * 每个请求路径一旦过滤器
     *
     * @param antPathMatcher 路径匹配器
     * @param urlPathHelper  url路径的助手
     */
    public AbstractPathOncePerRequestFilter(AntPathMatcher antPathMatcher,UrlPathHelper urlPathHelper){
        Assert.notNull(antPathMatcher,"path 匹配器不能为空");
        Assert.notNull(urlPathHelper,"PathHelper不能为空");
        this.antPathMatcher = antPathMatcher;
        this.urlPathHelper = urlPathHelper;
    }




    /**
     * Adds exclude url patterns.
     *
     * @param excludeUrlPatterns exclude urls
     */
    public void addExcludeUrlPatterns(@NonNull String... excludeUrlPatterns) {
        Assert.notNull(excludeUrlPatterns, "Exclude url patterns must not be null");
        Collections.addAll(this.excludeUrlPatterns, excludeUrlPatterns);
    }

    public void addUrlPatterns(String... urlPatterns) {
        Assert.notNull(urlPatterns, "UrlPatterns must not be null");
        Collections.addAll(this.urlPatterns, urlPatterns);
    }


    /**
     * Adds exclude url patterns.
     *
     * @param excludeUrlPatterns exclude urls
     */
    public void addExcludeUrlPatterns(Collection<String> excludeUrlPatterns) {
        Assert.notNull(excludeUrlPatterns, "Exclude url patterns must not be null");
        this.excludeUrlPatterns.addAll(excludeUrlPatterns);
    }

    public void addUrlPatterns(Collection<String> urlPatterns) {
        Assert.notNull(urlPatterns, "UrlPatterns must not be null");
        this.urlPatterns.addAll(urlPatterns);
    }



    /**
     * 不应该过滤
     *
     * @param request 请求
     * @return boolean
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        Assert.notNull(request, "Http servlet request must not be null");

        String uri = urlPathHelper.getRequestUri(request);
        // check white list
        boolean result = excludeUrlPatterns.stream().anyMatch(p -> antPathMatcher.match(p,uri ));

        return result || urlPatterns.stream().noneMatch(p -> antPathMatcher.match(p, uri));

    }



}
