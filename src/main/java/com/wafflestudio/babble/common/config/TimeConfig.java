package com.wafflestudio.babble.common.config;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {

    /**
     * 타임존에 따른 에러를 예방하기 위해 모든 시간 정보는 유닉스 시간 및 UTC 타임존을 기준으로 통일하여 관리한다. (참고. <a href="https://en.wikipedia.org/wiki/Unix_time">유닉스 시간</a>)
     */
    @PostConstruct
    void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
