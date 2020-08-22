package org.hehh.aliyun.afs.impl;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.afs.model.v20180112.AnalyzeNvcRequest;
import com.aliyuncs.afs.model.v20180112.AnalyzeNvcResponse;
import org.hehh.aliyun.afs.ITracelessMachineAuth;
import org.hehh.cloud.common.bean.result.Code;
import org.hehh.cloud.common.bean.result.ErrorResult;
import org.hehh.cloud.common.bean.result.Result;
import org.hehh.cloud.common.bean.result.SuccessResult;
import org.springframework.util.Assert;

/**
 * @author: HeHui
 * @date: 2020-03-25 16:07
 * @description: 无痕人机验证
 */
public class AliyunTracelessMachineAuth implements ITracelessMachineAuth {


    private final IAcsClient client;

    /**
     *  通过setScoreJsonStr方法声明"服务端调用人机验证服务接口得到的返回结果"与"前端执行操作"间的映射关系，并通知验证码服务端进行二次验证授权。
     *    注意：前端页面必须严格按照该映射关系执行相应操作，否则将导致调用异常。
     *    例如，在setScoreJsonStr方法中声明"400":"SC"，则当服务端返回400时，您的前端必须唤醒刮刮卡验证（SC），如果唤醒滑块验证（NC）则将导致失败。
     */
    private final String scoreJsonStr = "{\"200\":\"PASS\",\"400\":\"NC\",\"600\":\"SC\",\"700\":\"LC\",\"800\":\"BLOCK\"}";

    public AliyunTracelessMachineAuth(IAcsClient acsClient){
        Assert.notNull(acsClient,"阿里云请求类不能为空");
        this.client = acsClient;
    }




    /**
     * 认证 必填参数，由前端获取getNVCVal方法获得的值
     *
     * @param data
     * @return
     */
    @Override
    public Result auth(String data) {

        AnalyzeNvcRequest request = new AnalyzeNvcRequest();
        request.setData(data);

        /**
         * 根据业务需求设置各返回结果对应的客户端处置方式。
         */
        request.setScoreJsonStr(scoreJsonStr);
        try {
            AnalyzeNvcResponse response = client.getAcsResponse(request);
            if("100".equals(response.getBizCode())) {
                return SuccessResult.succeed(null);
            } else if ("200".equals(response.getBizCode())) {
                return SuccessResult.succeed(null);
            } else if ("400".equals(response.getBizCode())) {
                return ErrorResult.error(Code.AUTH_ERROR,"400");
            } else if ("600".equals(response.getBizCode())) {
                return ErrorResult.error(Code.AUTH_ERROR,"600");
            } else if ("700".equals(response.getBizCode())) {
                return ErrorResult.error(Code.AUTH_ERROR,"700");
            } else if ("800".equals(response.getBizCode())) {
                return ErrorResult.error(Code.AUTH_ERROR,"800");
            } else if ("900".equals(response.getBizCode())) {
                return ErrorResult.error(Code.AUTH_ERROR,"900");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ErrorResult.error("授权失败");
    }
}
