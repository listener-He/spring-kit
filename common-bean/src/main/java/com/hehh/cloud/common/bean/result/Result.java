package com.hehh.cloud.common.bean.result;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @author : hehh
 * @date : 2018/12/11 14:39
 * @describe : 返回类
 */
@AllArgsConstructor
@Data
public class Result<T> implements java.io.Serializable {

    protected Integer code;

    protected String msg;

    protected T data;

    protected Long timestamp;




    public Result msg(String msg){
         this.msg = msg;
         return this;
    }

    public Result data(T data){
        this.data = data;
        return this;
    }


    public Result code(int code){
        this.code = code;
        return this;
    }

    public Result code(Code code){
        return code(code.getCode());
    }




    public Result() {
        this.code = 0;
    }






    public long getTimestamp(){
         if(null == timestamp){
             this.timestamp =  System.currentTimeMillis();
         }
          return timestamp;
    }
}
