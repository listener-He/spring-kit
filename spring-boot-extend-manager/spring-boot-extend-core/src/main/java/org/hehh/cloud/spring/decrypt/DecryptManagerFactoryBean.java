//package org.hehh.cloud.spring.decrypt;
//
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.FactoryBean;
//import org.springframework.beans.factory.InitializingBean;
//
///**
// * @author: HeHui
// * @date: 2020-04-04 14:31
// * @description: 解密管理器工厂
// */
//public class DecryptManagerFactoryBean implements FactoryBean<DecryptManager>, InitializingBean, DisposableBean {
//
//
//
//    private final DecryptManager decryptManager;
//
//
//
//    public DecryptManagerFactoryBean(DecryptManager decryptManager){
//        this.decryptManager = decryptManager;
//    }
//
//
//
//
//
//
//    /**
//     *  销毁bean
//     * @throws Exception
//     */
//    @Override
//    public void destroy() throws Exception {
//
//    }
//
//
//
//
//
//
//    @Override
//    public DecryptManager getObject() throws Exception {
//        return decryptManager;
//    }
//
//
//
//    @Override
//    public Class<?> getObjectType() {
//        return decryptManager != null ? decryptManager.getClass() : DecryptManager.class;
//    }
//
//
//
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        super.
//    }
//}
