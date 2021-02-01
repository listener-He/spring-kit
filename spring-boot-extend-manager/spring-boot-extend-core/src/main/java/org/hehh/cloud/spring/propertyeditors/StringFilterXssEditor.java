package org.hehh.cloud.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

/**
 * @author: HeHui
 * @date: 2021-02-01 17:08
 * @description: sting xss 编辑器
 */
public class StringFilterXssEditor extends PropertyEditorSupport {


    private final boolean html;
    private final boolean javaScript;
    private final boolean sql;

    /**
     * 字符串过滤xss编辑器
     *
     * @param html       超文本标记语言
     * @param javaScript java脚本
     * @param sql        sql
     */
    public StringFilterXssEditor(boolean html, boolean javaScript, boolean sql) {
        this.html = html;
        this.javaScript = javaScript;
        this.sql = sql;
    }

    /**
     * 字符串过滤xss编辑器
     */
    public StringFilterXssEditor() {
        this(true, true, true);
    }

    /**
     * Sets the property value by parsing a given String.  May raise
     * java.lang.IllegalArgumentException if either the String is
     * badly formatted or if this kind of property can't be expressed
     * as text.
     *
     * @param text The string to be parsed.
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null) {
            setValue(null);
        } else {
            String value = text;
            if (html) {
                value =  null;// 处html字符串;
            }
            if (javaScript) {
                value = this.escapeScript(value);
            }
            if (sql) {
                value = this.escapeSql(value);
            }
            setValue(value);
        }
    }


    /**
     * 剥离SQL注入部分代码
     *
     * @param value
     *
     * @return
     */
    public String escapeSql(String value) {
        return value.replaceAll("('.+--)|(--)|(\\|)|(%7C)", "");
    }

    /**
     * 剥离js注入
     *
     * @param value
     *
     * @return
     */
    public String escapeScript(String value) {
        value = value.replace("script", "\\script").replace("/script", "\\/script");
        return value;
    }

}
