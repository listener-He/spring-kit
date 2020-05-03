package org.hehh.cloud.spring.mvc.request;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: HeHui
 * @date: 2020-03-28 17:08
 * @description: 替换InputStream request
 */
public class ReplaceInputStreamHttpServletRequest extends HttpServletRequestWrapper {


    private ServletInputStream replaceInputStream;


    public ReplaceInputStreamHttpServletRequest(HttpServletRequest request,byte[] data_byte){
        super(request);
        this.replaceInputStream = new BodyInputStream(data_byte);
    }

    public ReplaceInputStreamHttpServletRequest(HttpServletRequest request,InputStream body){
        super(request);
        this.replaceInputStream = new BodyInputStream(body);
    }


    /**
     * The default behavior of this method is to return getInputStream() on the
     * wrapped request object.
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(replaceInputStream != null){
            return replaceInputStream;
        }
        return super.getInputStream();
    }






    private static class BodyInputStream extends ServletInputStream {

        private final InputStream delegate;

        public BodyInputStream(InputStream inputStream){
            this.delegate  = inputStream;
        }

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
