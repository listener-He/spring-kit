package org.hehh.cloud.auth.interceptor;

import org.hehh.cloud.auth.bean.login.LoginUser;
import org.hehh.cloud.auth.holder.UserHolder;
import org.hehh.cloud.auth.token.TokenManager;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-09-16 01:36
 * @description: 服务端用户解析拦截器
 */
public class ServiceUserParsingInterceptor<T extends LoginUser> extends AbstractUserParsingInterceptor<T> {

    private final TokenManager<T> tTokenManager;

    public ServiceUserParsingInterceptor(String tokenName, UserHolder userHolder, TokenManager<T> tTokenManager) {
        super(tokenName, userHolder);
        Assert.notNull(tTokenManager,"TokenManager not null");
        this.tTokenManager = tTokenManager;
    }



    /**
     * 解析
     *
     * @param token    令牌
     * @param request
     * @param response
     * @return {@link Optional <T>}
     */
    @Override
    protected Optional parsing(String token, HttpServletRequest request, HttpServletResponse response) {
        return Optional.ofNullable(tTokenManager.getUser(token));
    }
}
