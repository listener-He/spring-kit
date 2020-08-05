package org.hehh.cloud.spring.mvc.config;

import org.hehh.cloud.spring.decrypt.*;
import org.hehh.cloud.spring.decrypt.adapter.RequestBodyDecryptAdapter;
import org.hehh.cloud.spring.decrypt.adapter.RequestFormDecryptAdapter;
import org.hehh.cloud.spring.decrypt.param.DecryptParameter;
import org.hehh.cloud.spring.mvc.crypto.CryptoConfiguration;
import org.hehh.cloud.spring.mvc.crypto.IDecryptAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;

/**
 * @author: HeHui
 * @create: 2020-03-23 01:18
 * @description: 加解密配置
 **/
@Configuration
@AutoConfigureAfter(RequestAutoConfiguration.class)
public class CryptoConfigurerComposite implements CryptoConfiguration {

    @Autowired
    private DecryptParameter decryptParameter;


    @Autowired
    private DecryptManager decryptManager;



    /**
     * 获取解密适配器
     *
     * @return
     */
    @Override
    public List<IDecryptAdapter> getDecryptAdapters() {
        return Arrays.asList(new RequestBodyDecryptAdapter(decryptManager, decryptParameter), new RequestFormDecryptAdapter(decryptManager, decryptParameter));
    }

}
