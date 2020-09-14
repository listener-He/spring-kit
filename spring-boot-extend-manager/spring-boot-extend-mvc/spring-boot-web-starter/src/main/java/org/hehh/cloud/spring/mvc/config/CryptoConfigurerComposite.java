package org.hehh.cloud.spring.mvc.config;

import org.hehh.cloud.spring.decrypt.*;
import org.hehh.cloud.spring.decrypt.adapter.RequestBodyDecryptAdapter;
import org.hehh.cloud.spring.decrypt.adapter.RequestFormDecryptAdapter;
import org.hehh.cloud.spring.decrypt.param.DecryptParameter;
import org.hehh.cloud.spring.mvc.crypto.CryptoConfiguration;
import org.hehh.cloud.spring.mvc.crypto.IDecryptAdapter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

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

    private final DecryptParameter decryptParameter;


    private final DecryptManager decryptManager;

    /**
     * 加密配置组合
     *
     * @param decryptParameter 解密参数
     * @param decryptManager   解密经理
     */
    public CryptoConfigurerComposite(DecryptParameter decryptParameter, DecryptManager decryptManager) {
        this.decryptParameter = decryptParameter;
        this.decryptManager = decryptManager;
    }


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
