package org.hehh.cloud.auth.holder;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.hehh.cloud.auth.bean.login.LoginUser;

/**
 * @author: HeHui
 * @date: 2020-09-16 00:53
 * @description: ali线程隔离用户存储
 */
public class TTLUserThreadLocalHolder<T extends LoginUser> extends UserThreadLocalHolder<T> {

    public TTLUserThreadLocalHolder() {
        super(new TransmittableThreadLocal<>());
    }
}
