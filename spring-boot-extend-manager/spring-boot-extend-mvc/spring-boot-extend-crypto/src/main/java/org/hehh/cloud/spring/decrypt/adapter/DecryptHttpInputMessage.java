package org.hehh.cloud.spring.decrypt.adapter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.InputStream;

/**
 * @author: HeHui
 * @create: 2020-03-21 16:07
 * @description: 解密请求报文
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecryptHttpInputMessage implements HttpInputMessage {


    private InputStream body;

    private HttpHeaders httpHeaders;


    /**
     * Return the body of the message as an input stream.
     *
     * @return the input stream body (never {@code null})
     */
    @Override
    public InputStream getBody() {
        return this.body;
    }

    /**
     * Return the headers of this message.
     *
     * @return a corresponding HttpHeaders object (never {@code null})
     */
    @Override
    public HttpHeaders getHeaders() {
        return this.httpHeaders;
    }
}
