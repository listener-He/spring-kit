package org.hehh.cloud.spring.mvc.http;

import org.hehh.cloud.spring.mvc.util.ObjectMapperKit;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-04-12 17:29
 * @description: 请求参数名值请求消息
 */
public class RequestNameValueHttpInputMessage  extends ServletServerHttpRequest {


    private InputStream inputStream;





    public RequestNameValueHttpInputMessage(HttpServletRequest request, String body, String name) throws Exception {
        super(request);
        Assert.hasText(name, "Request name must not be null");



        /**
         *  读取数据
         */
        if(StringUtils.hasText(body)){
            Map<String, Object> map = ObjectMapperKit.json2MapRecursion(body);
            if(map != null){
                Object o = map.get(name);
                if(o != null){
                    String s = ObjectMapperKit.toJsonStr(o);
                    inputStream = new ByteArrayInputStream(s.getBytes());
                }
            }

        }



    }









    /**
     * Return the body of the message as an input stream.
     *
     * @return the input stream body (never {@code null})
     * @throws IOException in case of I/O errors
     */
    @Override
    public InputStream getBody() throws IOException {
        if(inputStream != null){
            return inputStream;
        }
        return super.getBody();
    }






    private static class BodyInputStream extends ServletInputStream {

        private final InputStream delegate;

        public BodyInputStream(byte[] body) {
            this.delegate = new ByteArrayInputStream(body);
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read() throws IOException {
            return this.delegate.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return this.delegate.read(b, off, len);
        }

        @Override
        public int read(byte[] b) throws IOException {
            return this.delegate.read(b);
        }

        @Override
        public long skip(long n) throws IOException {
            return this.delegate.skip(n);
        }

        @Override
        public int available() throws IOException {
            return this.delegate.available();
        }

        @Override
        public void close() throws IOException {
            this.delegate.close();
        }

        @Override
        public synchronized void mark(int readlimit) {
            this.delegate.mark(readlimit);
        }

        @Override
        public synchronized void reset() throws IOException {
            this.delegate.reset();
        }

        @Override
        public boolean markSupported() {
            return this.delegate.markSupported();
        }
    }

}
