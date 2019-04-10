package org.bdilab.grrs.bic.service.result;

/**
 * @author caytng@163.com
 * @date 2019/4/10
 */
public class ResponseResultUtil {
    public static ResponseResult buildFailedResult(String errmsg) {
        return new ResponseResult(200,errmsg);
    }

    public static ResponseResult buildSuccessfulResult(Object data) {
        return new ResponseResult(200, "success", data, data.getClass());
    }
}
