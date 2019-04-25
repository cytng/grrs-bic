package org.bdilab.grrs.bic.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author caytng@163.com
 * @date 2019/4/10
 */
public class ResponseResultUtil {

    public static ResponseEntity tooManyObjects() {
        return new ResponseEntity<>("无法确定操作对象", HttpStatus.CONFLICT);
    }

    public static ResponseEntity wrongParameters() {
        return new ResponseEntity<>("参数有误", HttpStatus.NOT_ACCEPTABLE);
    }

    public static ResponseEntity missingSession() {
        return new ResponseEntity<>("未认证", HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity withoutPermmision() {
        return new ResponseEntity<>("没有操作权限", HttpStatus.NOT_ACCEPTABLE);
    }

    public static ResponseEntity internalError(Throwable throwable) {
        return new ResponseEntity<>(throwable, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity done() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    public static ResponseEntity failure(String errmsg) {
        return new ResponseEntity<>(errmsg, HttpStatus.METHOD_FAILURE);
    }

    public static <T> ResponseEntity<T> success(T data) {
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    public static <T> ResponseEntity<T> accepted(T data) {
        return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
    }

    public static <T> ResponseEntity<T> created(T data) {
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

}
