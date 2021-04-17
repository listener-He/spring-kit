package org.hehh.utils.http;

import cn.hutool.core.date.DateUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.hehh.utils.date.DateKit;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-08-09 20:15
 * @description: http client 请求参数处理器
 */
public class NameValuePairBuild {


//    public static List<NameValuePair> processor(Map<String,Object> param){
//        List<NameValuePair> pairList = new ArrayList<>(param.size());
//
//        param.forEach((k,v)->{
//            if(v instanceof File){
//                throw new RuntimeException("无法处理file类型");
//            }
//
//            String value = null;
//            if(v instanceof String || v instanceof Number){
//                value = v.toString();
//            }else if(v instanceof Date){
//                value = DateUtil.formatDateTime((Date)v);
//            }
//
//            pairList.add(new BasicNameValuePair(k,v.toString()));
//        });
//
//        return pairList;
//    }



    public static List<NameValuePair> processor(Map<String,String> param){
        return param.entrySet().stream().map(v->{
            return new BasicNameValuePair(v.getKey(),v.getValue());
        }).collect(Collectors.toList());
    }
}
