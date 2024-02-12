package com.wafflestudio.babble.common.domain;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false)
    private Long createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Long updatedAt;

    public Long getCreatedAtInSec() {
        return createdAt / 1000;
    }
}
