package org.hehh.framework.gateway.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.hehh.core.result.Code;
import org.hehh.core.result.ErrorResult;
import org.hehh.core.result.Result;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author : HeHui
 * @date : 2019-03-12 10:30
 * @describe : 异常时用JSON代替HTML异常信息
 */
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {


    private final ErrorProperties errorProperties;

    /**
     * Create a new {@code DefaultErrorWebExceptionHandler} instance.
     *
     * @param errorAttributes    the error attributes
     * @param resources          the resources configuration properties
     * @param errorProperties    the error configuration properties
     * @param applicationContext the current application context
     * @since 2.4.0
     */
    public JsonExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resources, errorProperties, applicationContext);
        this.errorProperties = errorProperties;
    }


    /**
     * 指定响应处理方法为JSON处理的方法
     *
     * @param errorAttributes
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(acceptsTextHtml(), this::renderErrorView)
            .andRoute(RequestPredicates.all(), this::renderErrorResponse);
    }


    /**
     * 根据code获取对应的HttpStatus
     *
     * @param errorAttributes
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        if (errorAttributes.get("data") == null || errorAttributes.get("data") == "null") {
            return HttpStatus.OK.value();
        }
        return Integer.valueOf(errorAttributes.getOrDefault("data", HttpStatus.OK).toString());

    }


    /**
     * Extract the error attributes from the current request, to be used to populate error
     * views or JSON payloads.
     *
     * @param request the source request
     * @param options options to control error attributes
     * @return the error attributes as a Map
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        return super.getErrorAttributes(request, options);
    }

    /**
     * 是否包含trace
     *
     * @param request
     * @return
     */
    private boolean getTraceParameter(ServerRequest request) {
        Optional<Object> trace = request.attribute("trace");
        if (!trace.isPresent()) {
            return false;
        }
        return !"false".equals(trace.get() + "".toLowerCase());
    }

    /**
     * 获取错误编码
     *
     * @param request
     * @return
     */
    private HttpStatus getStatus(ServerRequest request) {
        Optional<Object> attribute = request.attribute("javax.servlet.error.status_code");
        Integer statusCode = attribute.isPresent() ? (Integer) attribute.get() : null;
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }


    /**
     * 构建异常信息
     *
     * @param request
     * @param ex
     * @return
     */
    private Result<Void> buildMessage(ServerRequest request, Throwable ex) {
        final Result[] result = {new Result()};
        request.attributes().forEach((key, value) -> {
            Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.APPLICATION_JSON));
            Object status1 = body.get("status");
            HttpStatus status = null;
            if (null != status1) {
                status = HttpStatus.valueOf(Integer.valueOf(status1.toString()));
            } else {
                status = getStatus(request);
            }

            if (status.is5xxServerError()) {
                result[0] = ErrorResult.error(Code.FATAL_ERROR, "服务器繁忙.");
            } else if (status.is4xxClientError()) {
                switch (status.value()) {
                    case 400:
                        String message = body.get("message").toString();
                        if (StrUtil.isNotBlank(message) && message.length() > 3) {
                            int i = message.indexOf("'");
                            if (i == -1) {
                                if (message.contains("request body is missing")) {
                                    result[0] = ErrorResult.error(Code.PARAM_ERROR, "缺少body请求体.至少需要一个{}");
                                    break;
                                }
                                result[0] = ErrorResult.error(Code.PARAM_ERROR, "参数格式错误.");
                                break;
                            }
                            String substring = message.substring(i + 1, message.lastIndexOf("'"));
                            if (substring.indexOf("'") != -1 || substring.indexOf(".") != -1 || substring.indexOf(" ") != -1) {
                                result[0] = ErrorResult.error(Code.PARAM_ERROR, "参数类型错误.");
                                break;
                            }
                            result[0] = ErrorResult.error(Code.PARAM_ERROR, "缺少必填参数:" + substring);
                            break;
                        }
                        result[0] = ErrorResult.error(Code.PARAM_ERROR, "参数类型错误.");
                        break;
                    case 404:
                        result[0] = ErrorResult.error(Code.REST_ERROR, "请求资源不存在.");
                        break;
                    case 405:
                        result[0] = ErrorResult.error(Code.REST_METHOD_ERROR, "请求方法错误");
                        break;
                    case 428:
                        result[0] = ErrorResult.error(Code.BLACK_ERROR, "您已入系统访问黑名单,如有疑问请联系管理员!");
                        break;
                    case 429:
                        result[0] = ErrorResult.error(Code.LIMITING_ERROR, "请勿频繁访问!");
                        break;
                    default:
                        result[0] = ErrorResult.error(Code.HTTP_4XX_ERROR, "请求方式错误");
                }

            } else {
                result[0] = ErrorResult.error(Code.FATAL_ERROR, "服务器繁忙.");
            }


            /**
             *  如果不是异步 同时不是静态文件
             */
            List<MediaType> accept = request.headers().accept();
            if ((CollUtil.isEmpty(accept) || accept.stream().anyMatch(v -> v.equals(MediaType.TEXT_HTML)))) {
                /**
                 *  返回原状态码
                 */
                result[0].setData(status.value());

            }
        });


        if (ex != null) {
            if (ex instanceof NotFoundException) {
                result[0].setCode(Code.LINK_ERROR.getCode());
                result[0].setMsg("服务器连接中..,请稍后重试!");
            } else if (StrUtil.isNotBlank(ex.getMessage()) && ex.getMessage().startsWith("Connection refused")) {
                result[0].setCode(Code.LINK_ERROR.getCode());
                result[0].setMsg("服务器更新启动中...,请稍后重试");
            }
        }

        return result[0];


    }

    /**
     * 构建返回的JSON数据格式
     *
     * @return
     */
    private static Map<String, Object> response(Result<Void> result) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", result.getCode());
        map.put("msg", result.getMsg());
        if (null != result.getData()) {
            map.put("data", result.getData());
        }
        return map;
    }


}
