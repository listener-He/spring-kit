package org.hehh.aliyun.afs;


import org.hehh.cloud.common.bean.result.Result;

/**
 * @author: HeHui
 * @date: 2020-03-25 15:57
 * @description: 无痕人机认证接口
 */
public interface ITracelessMachineAuth {


    /**
     *  认证 必填参数，由前端获取getNVCVal方法获得的值
     * @param data
     * @return
     */
    Result auth(String data);
}
