package com.hehh.cloud.common.bean.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : HeHui
 * @date : 2019-02-26 16:22
 * @describe :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResult<T> extends Result<T> {


    public static <T> SuccessResult<T> succeed(T data,String msg){
        SuccessResult<T> tSuccessResult = new SuccessResult<T>();
        tSuccessResult.setCode(Code.OK.getCode());
        tSuccessResult.setData(data);
        tSuccessResult.setMsg(msg);
        return tSuccessResult;
    }


    public static <T>SuccessResult<T> succeed(T data){
         return succeed(data,null);
    }



    public static SuccessResult succeed(){
        return succeed(null);
    }
}
