package org.hehh.cloud.spring.mvc.core;

import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: HeHui
 * @date: 2020-03-28 16:58
 * @description: copy request
 */
public class CopyNativeWebRequest extends ServletWebRequest {


    private HttpServletRequest request;


    public CopyNativeWebRequest(NativeWebRequest request,HttpServletRequest copyRequest){
        super(request.getNativeRequest(HttpServletRequest.class));
        this.request = copyRequest;
    }


    @Override
    public Object getNativeRequest() {
        if(request != null){
            return request;
        }
        return super.getNativeRequest();
    }






    @Override
    public <T> T getNativeRequest(Class<T> requiredType) {

        return WebUtils.getNativeRequest( (request == null ? getRequest() : request), requiredType);
    }
}
