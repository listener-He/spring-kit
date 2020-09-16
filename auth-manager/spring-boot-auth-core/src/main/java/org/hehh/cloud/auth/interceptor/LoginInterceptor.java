package org.hehh.cloud.auth.interceptor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hehh.cloud.auth.annotation.Login;
import org.hehh.cloud.auth.bean.login.LoginUser;
import org.hehh.cloud.auth.holder.UserHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: HeHui
 * @date: 2020-09-16 01:13
 * @description: 登陆拦截器
 */
@Slf4j
public class LoginInterceptor<T extends LoginUser> implements HandlerInterceptor {

    private final Map<HandlerMethod,ModelLogin> methodLoginMap = new ConcurrentHashMap<>(128);
    private final ModelLogin notLogin = new ModelLogin();

    private final UserHolder<T> userHolder;

    public LoginInterceptor(UserHolder<T> userHolder) {
        this.userHolder = userHolder;
    }

    /**
     * Intercept the execution of a handler. Called after HandlerMapping determined
     * an appropriate handler object, but before HandlerAdapter invokes the handler.
     * <p>DispatcherServlet processes a handler in an execution chain, consisting
     * of any number of interceptors, with the handler itself at the end.
     * With this method, each interceptor can decide to abort the execution chain,
     * typically sending an HTTP error or writing a custom response.
     * <p><strong>Note:</strong> special considerations apply for asynchronous
     * request processing. For more details see
     * {@link AsyncHandlerInterceptor}.
     * <p>The default implementation returns {@code true}.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return {@code true} if the execution chain should proceed with the
     * next interceptor or the handler itself. Else, DispatcherServlet assumes
     * that this interceptor has already dealt with the response itself.
     * @throws Exception in case of errors
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ModelLogin login = hasPermission(handler);
        boolean isMustLogin = null != login && login.isMust();
        if(!isMustLogin){ return true;}

        T user = userHolder.get();
        if(user == null){
            throw new LoginException("请先登录");
        }

        int[] userTypes = login.getUserType();
        if(userTypes == null || userTypes.length < 1){
            return true;
        }
        if(Arrays.binarySearch(userTypes,user.getUserType()) >= 0 ){
            return true;
        }

        log.error("请求类型非法,限度类型:{},请求用户类型:{}",userTypes,user.getUserType());

        throw new LoginException("请求类型非法");
    }

    /**
     *
     *   获取方法注解
     * @param handler
     * @return
     */
    private ModelLogin hasPermission(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ModelLogin limitRequest = methodLoginMap.get(handlerMethod);

            if(null == limitRequest){
                /**获取方法上的注解*/
                Login login = handlerMethod.getMethodAnnotation(Login.class);
                /**如果方法上的注解为空 则获取类的注解*/
                if (null == login) {
                    login = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Login.class);
                }
                if(login != null){
                    limitRequest = new ModelLogin(login);
                }else{
                    limitRequest = notLogin;
                }
                methodLoginMap.put(handlerMethod,limitRequest);
            }
            return limitRequest;

        }
        return null;
    }


    /**
     * 模式登录
     *
     * @author hehui
     * @date 2020/09/16
     */
    @Data
    static class ModelLogin {


        /**
         *  是否必须登陆
         * @return
         */
        private boolean must = false;

        /**
         *  用户类型
         * @return
         */
        private int[] userType;

        public ModelLogin(){}
        public ModelLogin(Login login){
           this.must = (login.isMust());
            this.userType = (login.userType());
        }
    }
}
