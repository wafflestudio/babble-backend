package com.wafflestudio.babble.chat.domain;

import lombok.Getter;

@Getter
public enum ChatRoomHashTag {

    LECTURE_ROOM("강의실"),
    CAFETERIA("학생 식당"),
    RESTAURANT("식당"),
    LIBRARY("도서관"),
    DEPARTMENT_ROOM("과방"),
    CLUB_ACTIVITY_ROOM("동아리방"),
    CAFE("카페");

    private final String displayName;

    ChatRoomHashTag(String displayName) {
        this.displayName = displayName;
    }
}
