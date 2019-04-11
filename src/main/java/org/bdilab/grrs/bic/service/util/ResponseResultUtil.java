package org.bdilab.grrs.bic.service.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author caytng@163.com
 * @date 2019/4/10
 */
public class ResponseResultUtil {
    public static ResponseEntity wrongParameters() {
        return new ResponseEntity("参数有误", null, HttpStatus.ACCEPTED);
    }

    public static ResponseEntity failure(String errmsg) {
        return new ResponseEntity(errmsg, null, HttpStatus.ACCEPTED);
    }

    public static <T> ResponseEntity<T> success(T data) {
        return new ResponseEntity<>(data, null, HttpStatus.OK);
    }

    public static <T> ResponseEntity<T> created(T data) {
        return new ResponseEntity<>(data, null, HttpStatus.CREATED);
    }
}
