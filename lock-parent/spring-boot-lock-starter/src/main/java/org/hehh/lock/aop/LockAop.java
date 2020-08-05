package org.hehh.lock.aop;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hehh.lock.ILock;
import org.hehh.lock.annotations.Lock;
import org.springframework.context.ApplicationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-07-28 00:12
 * @description: 锁注解aop的实现
 * @see org.hehh.lock.ILock
 */
@Aspect
@Slf4j
@Order(-1)
public class LockAop {


    private final ILock defaultLock;

    @Setter
    private final ExpressionParser parser;

    private final Map<String,ILock> lockMap;

    @Setter
    private final ParameterNameDiscoverer parameterNameDiscoverer;

    @Setter
    private final ParserContext parserContext;
    /**
     * 锁aop
     *
     * @param defaultLock        默认的锁
     * @param applicationContext 应用程序上下文
     */
    public LockAop(ILock defaultLock, ApplicationContext applicationContext){
        this(defaultLock,new SpelExpressionParser(),applicationContext, new LocalVariableTableParameterNameDiscoverer(), new TemplateParserContext());
    }


    /**
     * 锁aop
     * @param defaultLock        默认的锁
     * @param parser             解析器
     * @param applicationContext 应用程序上下文
     * @param parameterNameDiscoverer
     * @param parserContext
     */
    public LockAop(ILock defaultLock, ExpressionParser parser, ApplicationContext applicationContext, ParameterNameDiscoverer parameterNameDiscoverer, ParserContext parserContext) {


        assert defaultLock != null : "lock类不能为空";
        assert applicationContext != null : "spring上下文对象不能为空";

        this.parserContext = parserContext;
        this.defaultLock = defaultLock;
        this.parser = parser;
        this.parameterNameDiscoverer = parameterNameDiscoverer;
        lockMap = applicationContext.getBeansOfType(ILock.class);
    }




    /**
     * 得到锁
     *
     * @param name 的名字
     * @return {@link ILock}
     */
    private ILock getLock(String name){
        if(lockMap == null){
            return defaultLock;
        }
        return lockMap.getOrDefault(name,defaultLock);
    }


    private String getKey(ProceedingJoinPoint joinPoint, Lock lockParameter){

        if(StringUtils.hasText(lockParameter.key())){
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            String[] params = parameterNameDiscoverer.getParameterNames(method);

            Object[] args = joinPoint.getArgs();

            EvaluationContext context = new StandardEvaluationContext();

            if(params != null){
                for (int len = 0; len < params.length; len++) {
                    context.setVariable(params[len], args[len]);
                }
            }

           return lockParameter.prefix() + parser.parseExpression(lockParameter.key(), parserContext).getValue(context, String.class);
        }

        return lockParameter.prefix();
    }


    /**
     *  环绕切面
     * @param joinPoint
     * @param lockParameter
     * @return
     * @throws Exception
     */
    @Around(value = "@annotation(lockParameter)")
    public Object doAround(ProceedingJoinPoint joinPoint, Lock lockParameter) throws Throwable {

        ILock lock = getLock(lockParameter.lock());
        String key = getKey(joinPoint, lockParameter);

        try {
            Optional<Object> optional = lock.mutex(key, lockParameter.timeout(), lockParameter.timeUnit(), () -> {
                Object[] args = joinPoint.getArgs();
                if (null != args) {
                    return joinPoint.proceed(args);
                }
                return joinPoint.proceed();
            });
        return optional.orElseThrow(() -> null);
        }finally {
            if(lockParameter.releaseLock()){
                lock.release(key);
            }
        }

    }

}
