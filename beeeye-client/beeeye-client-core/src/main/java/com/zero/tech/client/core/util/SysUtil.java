package com.zero.tech.client.core.util;

import com.zero.tech.base.constant.Constants;

/**
 * @desc 系统相关的util
 */
public class SysUtil {

    public static String host = Constants.EMPTY_STR;
    public static String userDir = Constants.EMPTY_STR;

    static {
        userDir = System.getProperty("user.dir", "<NA>");
    }
}
