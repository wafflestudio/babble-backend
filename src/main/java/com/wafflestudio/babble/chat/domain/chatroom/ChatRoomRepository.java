package com.wafflestudio.babble.chat.domain.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wafflestudio.babble.common.exception.NotFoundException;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    default ChatRoom get(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("존재하지 않는 채팅방입니다."));
    }
}
