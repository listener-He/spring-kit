package org.hehh.framework.gateway.ratelimit.script;

import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author: HeHui
 * @date: 2020-06-12 16:24
 * @description: 限流redis lua脚本 其配置参考
 * @see "GatewayRedisAutoConfiguration#redisRequestRateLimiterScript()"
 */
public class LimitingRedisScript<T>  extends DefaultRedisScript<T> {

    
    /**
     * Create a new LimitingRedisScript for the given resource.
     * @param resource the Resource to load the script from (using UTF-8 encoding)
     * @see  ResourceScriptSource Create
     */
    public LimitingRedisScript(Resource resource){
        super();
        super.setScriptSource(new ResourceScriptSource(resource));
    }


}
