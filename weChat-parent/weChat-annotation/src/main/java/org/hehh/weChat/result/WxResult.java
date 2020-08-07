package org.hehh.weChat.result;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hehh
 * @date : 2018/12/6 10:09
 * @describe : 微信响应
 */
@Data
@NoArgsConstructor
public class WxResult implements java.io.Serializable {

    /**响应状态码0为成功*/
    protected String errcode;

    /**响应消息ok为成功*/
    protected String errmsg;


    public WxResult(String errcode, String errmsg){
         this.errcode = errcode;
         this.errmsg = errmsg;
    }


    /**
     *  响应是否成功
     * @return
     */
    public boolean ok(){
        return errcode != null && errcode.equals("0");
    }




}
