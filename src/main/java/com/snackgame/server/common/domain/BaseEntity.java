package com.snackgame.server.common.domain;

import java.time.LocalDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @ColumnDefault("CURRENT_TIMESTAMP")
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @ColumnDefault("CURRENT_TIMESTAMP")
    protected LocalDateTime updatedAt;
}
