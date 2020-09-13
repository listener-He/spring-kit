package org.hehh.cloud.spring.mvc.request;

import cn.hutool.core.util.StrUtil;
import org.hehh.cloud.spring.mvc.tool.ObjectMapperKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author : HeHui
 * @date : 2020-03-29 02:19
 * @describe : 替换参数 request
 */
public class ReplaceParamHttpServletRequest extends HttpServletRequestWrapper {



    private Map<String,Object> paramMap;

    private final boolean emptyQueryString;
    public ReplaceParamHttpServletRequest(HttpServletRequest request, String param,boolean emptyQueryString){
        super(request);
        this.emptyQueryString =emptyQueryString;
        if(StrUtil.isNotBlank(param)){

            try {
                this.paramMap = ObjectMapperKit.json2MapRecursion(param);
                System.out.println(param);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public ReplaceParamHttpServletRequest(HttpServletRequest request, Map<String,String[]> param,boolean emptyQueryString){
        super(request);
        this.emptyQueryString =emptyQueryString;
        if(param != null){
            Map<String,Object> paramMap = new HashMap<>(param.size());
            param.forEach((k,v)->{
                if(v != null){
                    paramMap.put(k,v.length > 1 ? Arrays.asList(v) : v[0]);
                }
            });
            this.paramMap = paramMap;
        }


    }


    /**
     * The default behavior of this method is to return getQueryString() on the
     * wrapped request object.
     */
    @Override
    public String getQueryString() {
        if(emptyQueryString){
            return null;
        }
        return super.getQueryString();
    }

    /**
     * The default behavior of this method is to return getParameter(String
     * name) on the wrapped request object.
     *
     * @param name
     */
    @Override
    public String getParameter(String name) {
        if(null == paramMap){
            return super.getParameter(name);
        }


        String[] parameterValues = getParameterValues(name);


        return parameterValues == null  ? null : parameterValues[0];
    }


    /**
     * The default behavior of this method is to return getParameterMap() on the
     * wrapped request object.
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        if(paramMap == null){
            return super.getParameterMap();
        }

        Map<String,String[]> resultMap = new HashMap<>(paramMap.size());

        paramMap.keySet().forEach(v->{
            resultMap.put(v,getParameterValues(v));
        });

        return resultMap;
    }

    /**
     * The default behavior of this method is to return getParameterNames() on
     * the wrapped request object.
     */
    @Override
    public Enumeration<String> getParameterNames() {
        if(paramMap == null){
            return super.getParameterNames();
        }

        return Collections.enumeration(paramMap.keySet());

    }

    /**
     * The default behavior of this method is to return
     * getParameterValues(String name) on the wrapped request object.
     *
     * @param name
     */
    @Override
    public String[] getParameterValues(String name) {
        if(paramMap == null){
            return super.getParameterValues(name);
        }

        Object o = paramMap.get(name);
        if(o == null){
            return null;
        }

        if(o instanceof Collection){
            List<Object> value = ((List<Object>)o);
           return value.stream().filter(v->v !=null).map(String::valueOf).collect(Collectors.toList()).toArray(new String[value.size()]);
        }

        return new String[]{o.toString()};

    }


}
