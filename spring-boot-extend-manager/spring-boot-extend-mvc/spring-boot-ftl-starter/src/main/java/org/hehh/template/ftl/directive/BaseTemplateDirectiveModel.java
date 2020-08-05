package org.hehh.template.ftl.directive;

import freemarker.template.TemplateDirectiveModel;

/**
 * @author: HeHui
 * @create: 2019-11-20 11:57
 * @description: TemplateDirectiveModel 基类
 **/
public interface BaseTemplateDirectiveModel extends TemplateDirectiveModel {
    /**
     *  标签名
     * @return
     */
    String tagName();
}
