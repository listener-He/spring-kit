package org.hehh.sitemap.bean;

/**
 * @author: HeHui
 * @date: 2020-08-16 18:36
 * @description: 更改频率
 */
public enum ChangeFreq {

    ALWAYS, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY, NEVER;
    String lowerCase;

    private ChangeFreq() {
        lowerCase = this.name().toLowerCase();
    }

    @Override
    public String toString() {
        return lowerCase;
    }
}
