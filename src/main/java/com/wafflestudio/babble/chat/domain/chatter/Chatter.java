package com.wafflestudio.babble.chat.domain.chatter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.wafflestudio.babble.chat.domain.chatroom.ChatRoom;
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
public class Chatter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Embedded
    private Nickname nickname;

    public static Chatter create(ChatRoom room, Member member, String nickname) {
        return new Chatter(0L, room, member, new Nickname(nickname));
    }

    public Long getRoomId() {
        return room.getId();
    }

    public Long getMemberId() {
        return member.getId();
    }

    public void updateNickname(String nickname) {
        this.nickname = new Nickname(nickname);
    }

    public String getNickname() {
        return nickname.getValue();
    }
}
