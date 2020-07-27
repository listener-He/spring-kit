package org.hehh.lock;

/**
 * @author: HeHui
 * @date: 2020-07-27 22:33
 * @description: 锁回调
 */
@FunctionalInterface
public interface LockCallback<T> {


    /**
     *  拿到锁了，等待释放
     * @return
     */
    T doInLock() throws Throwable;
}
