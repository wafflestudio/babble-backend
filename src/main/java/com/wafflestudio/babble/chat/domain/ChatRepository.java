package com.wafflestudio.babble.chat.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wafflestudio.babble.common.exception.NotFoundException;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    default Chat getByIdAndRoom(Long id, ChatRoom room) {
        return findByIdAndRoom(id, room).orElseThrow(() -> new NotFoundException("존재하지 않는 채팅입니다."));
    }

    Optional<Chat> findByIdAndRoom(Long id, ChatRoom room);

    List<Chat> findAllByRoom(ChatRoom room);

    List<Chat> findAllByRoomAndIdGreaterThan(ChatRoom room, Long chatId);
}
