package org.hehh.utils.file.config;

/**
 * @author: HeHui
 * @date: 2021-01-27 14:10
 * @description: 文件工具参数
 */
public class FileToolParameters {

    /**
     * freemarker模板目录
     */
    private String freemarkerTemplate = "classpath:/template";


    public String getFreemarkerTemplate() {
        return freemarkerTemplate;
    }

    public void setFreemarkerTemplate(String freemarkerTemplate) {
        this.freemarkerTemplate = freemarkerTemplate;
    }
}
