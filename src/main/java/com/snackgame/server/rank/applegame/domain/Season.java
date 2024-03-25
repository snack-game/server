package com.snackgame.server.rank.applegame.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public Season(String name, LocalDateTime startedAt) {
        this.name = name;
        this.startedAt = startedAt;
    }

    public Season(String name, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.name = name;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    @Override
    public String toString() {
        return "Season{" +
               "name='" + name + '\'' +
               '}';
    }
}
