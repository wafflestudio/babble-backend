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
import com.wafflestudio.babble.common.exception.BadRequestException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
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

    private Chat(Long id, ChatRoom room, Chatter chatter, String content, Chat parentChat) {
        this.id = id;
        this.room = room;
        this.chatter = chatter;
        this.content = getValidatedContent(content);
        this.parentChat = parentChat;
    }

    private static String getValidatedContent(String content) {
        content = content.trim();
        if (content.isEmpty()) {
            throw new BadRequestException("채팅은 공백일 수 없다.");
        }
        if (content.length() > 2000) {
            throw new BadRequestException("채팅은 2000글자 이내여야 한다.");
        }
        return content;
    }

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
