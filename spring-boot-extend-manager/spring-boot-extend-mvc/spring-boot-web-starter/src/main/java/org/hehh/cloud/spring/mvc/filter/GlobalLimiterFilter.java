package org.hehh.cloud.spring.mvc.filter;

import org.hehh.cloud.spring.mvc.exception.GlobalLimiterException;
import org.hehh.cloud.spring.mvc.parameter.GlobalLimitingParameter;
import org.hehh.security.limiter.GuavaRateLimiter;
import org.hehh.security.limiter.Limiter;
import org.hehh.security.limiter.LimiterException;
import org.hehh.security.limiter.SemaphoreLimiter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-07-26 17:30
 * @description: 全局限流过滤器
 */
public class GlobalLimiterFilter extends AbstractPathOncePerRequestFilter {


    private final Limiter<Void> limiter;

    private final long timeout ;

    private final TimeUnit unit;


    /**
     * 全局限幅器过滤器
     *
     * @param parameter 参数
     */
    public GlobalLimiterFilter(GlobalLimitingParameter parameter){

        Assert.notNull(parameter,"限流参数不能为空");

        switch (parameter.getType()){
            case RATE:
                if(parameter.getRate().getWarMupPeriod() != null && parameter.getRate().getUnit() != null){
                    limiter = new GuavaRateLimiter<Void>(parameter.getRate().getPermitsPerSecond(),parameter.getRate().getWarMupPeriod(),parameter.getRate().getUnit(),parameter.getPermits());
                }else{
                    limiter = new GuavaRateLimiter<Void>(parameter.getRate().getPermitsPerSecond(),parameter.getPermits());
                }
                break;
            case SEMAPHORE:
                limiter = new SemaphoreLimiter<>(parameter.getSemaphore().getCapacity(),parameter.getPermits(),parameter.getSemaphore().getMaxQueueLength());
                break;
            default:
                throw new IllegalArgumentException("限流类型错误!");
        }


        if(!CollectionUtils.isEmpty(parameter.getExcludePatterns())){
            super.addExcludeUrlPatterns(parameter.getExcludePatterns());
        }

        if(!CollectionUtils.isEmpty(parameter.getUrlPatterns())){
            super.addUrlPatterns(parameter.getUrlPatterns());
        }

        this.timeout = parameter.getTimeout();
        this.unit = parameter.getUnit();

    }



    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            limiter.acquire(timeout, unit,(x) -> { return null; });
        } catch (LimiterException e) {
            throw new GlobalLimiterException(e);
        }

    }
}
