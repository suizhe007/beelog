package com.zero.tech.base.util;

import org.slf4j.Logger;

import static com.zero.tech.base.constant.Constants.STRING_PROFILES_ACTIVE;
import static com.zero.tech.base.constant.Constants.STRING_PROFILES_ACTIVE_TEST;

public class Utils {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static void checkEnv(Logger LOGGER) {
        if (Utils.isNullOrEmpty(System.getProperty(STRING_PROFILES_ACTIVE))) {
            LOGGER.info("current environment is develop environment");
        } else {
            if (STRING_PROFILES_ACTIVE_TEST.equals(System.getProperty(STRING_PROFILES_ACTIVE))) {
                LOGGER.info("current environment is test environment");
            } else {
                LOGGER.info("current environment is production environment");
            }
        }
    }
}
