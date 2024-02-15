package com.wafflestudio.babble.chat.domain.chatroom;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import com.wafflestudio.babble.chat.domain.Location;
import com.wafflestudio.babble.common.domain.BaseEntity;
import com.wafflestudio.babble.member.domain.Member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String roomName;

    @JoinColumn(nullable = false)
    private Long managerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomHashTag hashTag;

    @Embedded
    private Location location;

    public static ChatRoom create(String roomName, Member member, ChatRoomHashTag hashTag, Location location) {
        return new ChatRoom(0L, roomName, member.getId(), hashTag, location);
    }
}
