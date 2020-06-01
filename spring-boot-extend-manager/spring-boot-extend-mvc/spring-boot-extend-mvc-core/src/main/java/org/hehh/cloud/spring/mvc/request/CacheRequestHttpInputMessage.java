package org.hehh.cloud.spring.mvc.request;

import org.springframework.http.server.ServletServerHttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: HeHui
 * @create: 2020-03-22 22:30
 * @description: 缓存请求输入流
 **/
public class CacheRequestHttpInputMessage extends ServletServerHttpRequest {




    private InputStream inputStream;


    /**
     * Construct a new instance of the ServletServerHttpRequest based on the
     * given {@link HttpServletRequest}.
     *
     * @param servletRequest the servlet request
     */
    public CacheRequestHttpInputMessage(HttpServletRequest servletRequest) {
        super(servletRequest);
    }


    @Override
    public InputStream getBody() throws IOException {
        if(inputStream == null){
            this.inputStream = super.getBody();
        }
        return this.inputStream;
    }
}

