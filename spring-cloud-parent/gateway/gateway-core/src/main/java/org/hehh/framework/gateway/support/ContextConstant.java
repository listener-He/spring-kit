package org.hehh.framework.gateway.support;


/**
 * 上下文常量
 *
 * @author HeHui
 * @date 2021-06-06 19:20
 */
public class ContextConstant {


    public static final String USER_AGENT_PARSE = qualify("user_agent_parse");


    private static String qualify(String attr) {
        return ContextConstant.class.getName() + "." + attr;
    }
}
