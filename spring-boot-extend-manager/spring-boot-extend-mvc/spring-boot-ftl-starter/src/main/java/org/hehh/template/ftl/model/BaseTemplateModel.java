package org.hehh.template.ftl.model;


import freemarker.template.TemplateMethodModelEx;

/**
 * @author: HeHui
 * @create: 2019-12-05 09:55
 * @description: 基类TemplateModel
 **/
public interface BaseTemplateModel extends TemplateMethodModelEx {

    /**
     *  名称
     * @return
     */
    String name();


    /**
     *  TemplateMethodModelEx.exec(list) 方法
     *
     *      exec方法中可以传递多个参数 通过索引来获取
     *
     */

}
