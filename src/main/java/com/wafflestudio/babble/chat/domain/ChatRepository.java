package com.wafflestudio.babble.chat.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

   List<Chat> findAllByRoomAndIdGreaterThan(ChatRoom room, Long chatId);
}
