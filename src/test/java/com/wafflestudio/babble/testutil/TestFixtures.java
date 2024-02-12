package com.wafflestudio.babble.testutil;

import com.wafflestudio.babble.chat.domain.Location;

public class TestFixtures {

    public static final String USER_ID = "ABC123";
    public static final Long KAKAO_AUTH_ID = 123L;
    public static final String NICKNAME = "멋진하마";
    public static final String ROOM_NAME = "101동 수업 강의실";
    public static final String HASHTAG = "LECTURE_ROOM";
    public static final String CHAT_CONTENT = "안녕하세요";
    public static final Location LOCATION = new Location(10.0d, 10.0d);
    public static final Double LATITUDE = 10.0d;
    public static final Double LONGITUDE = 10.0d;
    public static final Long PAST_UNIX_TIME_MS = 1707696000L * 1000; // 2024년 2월 12일 0시 0분 0초 (ms 단위)
    public static final Long FUTURE_UNIX_TIME_MS = 32472144000L * 1000; // 2999년 1월 1일 0시 0분 0초 (ms 단위)
}
