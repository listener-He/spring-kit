package org.hehh.cloud.auth.holder;

import org.hehh.cloud.auth.bean.login.LoginUser;

/**
 * @author: HeHui
 * @date: 2020-09-16 01:29
 * @description: 用户保存
 */
public interface UserHolder<T extends LoginUser> {


    /**
     * 添加用户信息
     *
     * @param user
     */
    void add(T user);


    /**
     * 获取用户信息
     *
     * @return
     */
    T get();



    /**
     * 删除信息
     */
    void remove();
}
