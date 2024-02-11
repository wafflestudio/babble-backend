package com.wafflestudio.babble.chat.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatterRepository extends JpaRepository<Chatter, Long> {

    default boolean existsByRoomIdAndMemberId(Long roomId, Long memberId) {
        return countByRoomIdAndMemberId(roomId, memberId) > 0;
    }

    @Query("SELECT COUNT(*)"
        + "FROM Chatter c "
        + "JOIN c.room "
        + "JOIN c.member "
        + "WHERE c.room.id = :roomId AND c.member.id = :memberId")
    int countByRoomIdAndMemberId(@Param("roomId") Long roomId, @Param("memberId") Long memberId);
}
