package com.wafflestudio.babble.chat.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wafflestudio.babble.member.domain.Member;

@Repository
public interface ChatterRepository extends JpaRepository<Chatter, Long> {

    default boolean existsByRoomIdAndUserId(Long roomId, String userId) {
        return countByRoomIdAndUserId(roomId, userId) > 0;
    }

    @Query("SELECT COUNT(*)"
        + "FROM Chatter c "
        + "JOIN c.room "
        + "JOIN c.member "
        + "WHERE c.room.id = :roomId AND c.member.userId = :userId")
    int countByRoomIdAndUserId(@Param("roomId") Long roomId, @Param("userId") String userId);

    Optional<Chatter> findByRoomAndMember(ChatRoom room, Member member);
}
