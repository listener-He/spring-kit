package org.hehh.weChat;

import lombok.Data;

import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-08-06 19:53
 * @description: 配置参数
 */
@Data
public class WxConfigurationParameter {

    private List<APP> apps;



    static class APP {

        private String appId;

        private String appSecret;


        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public void setAppSecret(String appSecret) {
            this.appSecret = appSecret;
        }
    }
}
