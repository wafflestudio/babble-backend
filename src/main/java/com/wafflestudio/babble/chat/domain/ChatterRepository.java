package com.wafflestudio.babble.chat.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatterRepository extends JpaRepository<Chatter, Long> {
}
