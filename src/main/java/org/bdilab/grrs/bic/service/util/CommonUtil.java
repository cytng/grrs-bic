package org.bdilab.grrs.bic.service.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @author caytng@163.com
 * @date 2019/4/12
 */
public class CommonUtil {
    public static Boolean isNull(Object o) {
        return o == null;
    }

    public static Boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    public static Boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static Boolean isEmpty(Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }

    public static Boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
}
