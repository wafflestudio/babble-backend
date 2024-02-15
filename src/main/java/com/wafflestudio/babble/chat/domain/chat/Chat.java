package com.wafflestudio.babble.chat.domain.chat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.wafflestudio.babble.chat.domain.chatroom.ChatRoom;
import com.wafflestudio.babble.chat.domain.chatter.Chatter;
import com.wafflestudio.babble.common.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatter_id", nullable = false)
    private Chatter chatter;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_chat_id")
    private Chat parentChat;

    public static Chat create(ChatRoom room, Chatter chatter, String content) {
        return new Chat(0L, room, chatter, content, null);
    }

    public static Chat createChild(ChatRoom room, Chatter chatter, String content, Chat parentChat) {
        return new Chat(0L, room, chatter, content, parentChat);
    }

    public boolean hasParent() {
        return parentChat != null;
    }
}
