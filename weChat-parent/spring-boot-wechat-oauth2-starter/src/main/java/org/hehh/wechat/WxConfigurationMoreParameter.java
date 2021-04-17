package org.hehh.wechat;

import lombok.Data;
import org.hehh.weChat.WxConfigurationParameter;

import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-08-09 15:33
 * @description: 多微信配置参数
 */
@Data
public class WxConfigurationMoreParameter {

    
    private List<WxConfigurationParameter> apps;
}
