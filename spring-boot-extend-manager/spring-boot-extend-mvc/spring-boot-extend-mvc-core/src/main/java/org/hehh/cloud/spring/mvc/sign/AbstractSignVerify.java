package org.hehh.cloud.spring.mvc.sign;

import cn.hutool.core.util.ArrayUtil;
import org.hehh.cloud.spring.tool.SignUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-09-13 20:58
 * @description: 签名参数排序
 */
public abstract class AbstractSignVerify implements SignVerify {



    private final List<String> ignoreParams = new ArrayList<>();


    /**
     * 信号参数排序
     *
     * @param ignore 忽略
     */
    public AbstractSignVerify(String... ignore){
        if(ArrayUtil.isNotEmpty(ignore)){
            this.ignoreParams.addAll(Arrays.asList(ignore));
        }
    }






    /**
     * 签名
     *
     * @param body      body参数
     * @param params    参数个数
     * @param timestamp 时间戳
     * @param secretKey 加密密钥(加密盐)，为防止被穷举。为空则不加密
     * @param paths     路径
     * @return {@link String}
     */
    @Override
    public String sign(String body, Map<String, String> params, String secretKey,long timestamp, String... paths) throws SignatureException {

        if(StringUtils.isEmpty(body) && CollectionUtils.isEmpty(params) && ArrayUtil.isEmpty(paths)){
            throw new IllegalStateException("Param: [body form paths] It can't all be empty");
        }


        StringBuilder sb = new StringBuilder();

        /**
         *  拼接body参数
         */
        if (StringUtils.hasText(body)) {
            sb.append(body);
        }


        /**
         *  拼接表单参数
         */
        if (!CollectionUtils.isEmpty(params)) {
            if(sb.length() > 0){
                sb.append("&");
            }

            /**
             *  过滤空指和被忽略的参数
             */
            Map<String, String> sortParam = params.entrySet()
                .stream()
                .filter(v -> !ignoreParams.contains(v.getKey()) && StringUtils.hasText(v.getValue()))
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            Iterator<String> keys = sortParam.keySet().iterator();
            while (keys.hasNext()){
                String k = keys.next();
                sb.append(k).append("=");

                String v = sortParam.get(k);
//                if(encode){
//                    v = SignUtil.urlEncode(v,SignUtil.INPUT_CHARSET);
//                }

                /**
                 *  得到值
                 */
                sb.append(v);

                /**
                 *  如果还有参数
                 */
                if(keys.hasNext()){
                    sb.append("&");
                }
            }
        }

        /**
         *  拼接path上的参数
         */
        if (ArrayUtil.isNotEmpty(paths)) {
            String pathValues = String.join(",", Arrays.stream(paths).sorted().toArray(String[]::new));
            if(sb.length() > 0){
                sb.append("&");
            }

            sb.append(pathValues);
        }

        String digest = digest(sb.toString(), secretKey, timestamp) + timestamp;
        try {
            return SignUtil.encodeBase64Str(digest.getBytes(SignUtil.INPUT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new SignatureException("摘要encode异常",e);
        }
    }







    /**
     * 摘要
     *
     * @param param     参数
     * @param secretKey 秘密密钥
     * @param timestamp 时间戳
     * @return {@link String}
     */
    protected abstract String digest(String param, String secretKey, long timestamp) throws SignatureException;


    public static void main(String[] args) throws SignatureException {
        SignVerify sign = new Sha256SignVerify();

        Map<String,String> param = new HashMap<>();
        param.put("A1", "1");
        param.put("A2", "2");
        param.put("A3", "3");
        param.put("A5",null);

        String body = "{\"B\":\"1\"}";

        String[] path = {"31","24","32"};


        String key = "123456";
        long millis = System.currentTimeMillis();

        String s = sign.sign(body, param, key, millis,path);

        System.out.println("签名:"+s);

//        boolean verify = sign.verify(body, param, key,s, path,millis);
//
//        System.out.println(verify);
//        System.out.println("耗时"+(System.currentTimeMillis() - millis));


    }
}
