package org.hehh.cloud.common.bean.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;


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
        this.code = Code.OK.getCode();
    }






    public long getTimestamp(){
         if(null == timestamp){
             this.timestamp =  System.currentTimeMillis();
         }
          return timestamp;
    }


    /**
     *  是否成功
     * @return
     */
    public boolean isOk(){
        return this.code != null && code.equals(Code.OK.getCode());
    }

    /**
     *  Optional方式获取 data
     * @return
     */
    public Optional<T> optionalData(){
        return Optional.ofNullable(data);
    }


    /**
     * 转换成其他类型，注意 转换后 data就是null了
     * @param tClass 目标类型
     * @param <E>
     * @return
     */
    public <E> Result<E> conversion(Class<E> tClass){
        return new Result<E>(this.code, this.msg, null,this.timestamp);
    }
}
