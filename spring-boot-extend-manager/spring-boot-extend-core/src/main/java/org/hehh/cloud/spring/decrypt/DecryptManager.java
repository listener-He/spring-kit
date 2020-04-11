package org.hehh.cloud.spring.decrypt;

import org.hehh.cloud.spring.core.SpringContextKit;

import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-04-04 13:41
 * @description: 解密管理器
 */
public class DecryptManager{

    /**
     *  默认解密器
     */
    private IDecrypt defaultDecrypt;

    /**
     *  解密器集合
     */
    private Map<String,IDecrypt> decryptMap;


    private final SpringContextKit springContextKit;



    public DecryptManager(SpringContextKit springContextKit){
        this.springContextKit = springContextKit;
    }





    /**
     *  获取默认解密器
     * @return
     */
    public IDecrypt get(){
        if(null == defaultDecrypt){
            this.defaultDecrypt =  springContextKit.getBean(IDecrypt.class);
        }
        return defaultDecrypt;
    }




    /**
     *  根据bean名称获取解密器
     * @param beanName
     * @return
     */
    public IDecrypt get(String  beanName){
        if(decryptMap == null){
            decryptMap = springContextKit.getBeans(IDecrypt.class);
        }

        if(decryptMap != null){
            decryptMap.get(beanName);
        }

        return null;
    }

}
