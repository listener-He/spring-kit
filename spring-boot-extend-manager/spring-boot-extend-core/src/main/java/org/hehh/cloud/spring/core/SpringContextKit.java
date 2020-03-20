package org.hehh.cloud.spring.core;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @author : HeHui
 * @date : 2019-02-26 11:00
 * @describe : spring容器工具
 */
public class SpringContextKit implements ApplicationContextAware {

    private  ApplicationContext applicationContext = null;

    private  DefaultListableBeanFactory defaultListableBeanFactory = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext == null) {
             this.applicationContext = applicationContext;
        }

        if (null == defaultListableBeanFactory) {
            this.defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        }

    }


    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    /**
     * 根据bean名称获取实例
     *
     * @param name
     * @return
     */
    public Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 根据class名称获取实例
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);

    }


    /**
     * 获取父类 or 接口的 所有实现bean
     *
     * @param superClass
     * @param <T>
     * @return
     */
    public <T> Map<String, T> getBeans(Class<T> superClass) {
        return applicationContext.getBeansOfType(superClass);
    }


    /**
     * 注入bean到父类
     *
     * @param beanClass
     * @param bean
     * @param <T>
     */
    public <T> void registerBeanDefinition(Class<T> beanClass, T bean) {
        if (null == beanClass || null == bean) {
            return;
        }
        defaultListableBeanFactory.registerResolvableDependency(beanClass, bean);
        return;
    }


    /**
     * 注入bean
     *
     * @param beanClass  注入bean类型
     * @param values     普通参数
     * @param references 注入参数
     * @param <T>
     */
    public <T> void registerBeanDefinition(Class<T> beanClass, Map<String, Object> values, Map<String, String> references) {
        if (null == beanClass) {
            return;
        }

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);

        if (MapUtil.isNotEmpty(values)) {
            values.forEach((k, v) -> {
                builder.addPropertyValue(k, v);
            });
        }
        if (MapUtil.isNotEmpty(references)) {
            references.forEach((k, v) -> {
                builder.addPropertyReference(k, v);
            });
        }
        /**注册bean*/
        defaultListableBeanFactory.registerBeanDefinition(StrUtil.lowerFirst(ClassUtil.getClassName(beanClass, true)), builder.getRawBeanDefinition());

    }

}
