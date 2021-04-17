package org.hehh.cloud.auth.holder;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.hehh.cloud.auth.bean.login.LoginUser;

/**
 * @author: HeHui
 * @create: 2019-11-23 17:22
 * @description: 登陆用户线程之间的传递
 **/
public abstract class UserThreadLocalHolder<T extends LoginUser> implements UserHolder<T> {

    /**
     * 用户信息线程隔离存储
     */
    private final TransmittableThreadLocal<T> userThreadLocal;

    protected UserThreadLocalHolder(TransmittableThreadLocal<T> userThreadLocal) {
        this.userThreadLocal = userThreadLocal;
    }


    /**
     * 添加用户信息
     *
     * @param user
     */
    @Override
    public void add(T user) {
        if (null == user) {
            return;
        }
        userThreadLocal.set(user);
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    @Override
    public T get() {
        return userThreadLocal.get();
    }



    /**
     * 删除信息
     */
    @Override
    public void remove() {
        userThreadLocal.remove();
    }


}
