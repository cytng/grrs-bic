package org.bdilab.grrs.bic.service.result;

import lombok.Data;

/**
 * @author caytng@163.com
 * @date 2019/4/10
 */
@Data
public class ResponseResult<T> {
    private Integer code;
    private String message;
    private T data;
    private Class clazz;

    public ResponseResult(Integer code, String message) {
        setCode(code);
        setMessage(message);
    }

    public ResponseResult(Integer code, String message, T data, Class clazz) {
        this(code, message);
        setData(data);
        setClazz(clazz);
    }
}
