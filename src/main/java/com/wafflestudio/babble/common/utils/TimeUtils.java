package com.wafflestudio.babble.common.utils;

import java.util.Date;

public class TimeUtils {

    public static long getCurrentUnixTimestamp() {
        return new Date().getTime();
    }
}
