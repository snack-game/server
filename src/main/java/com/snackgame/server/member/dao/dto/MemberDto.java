package com.snackgame.server.member.dao.dto;

import java.util.Objects;

import com.snackgame.server.member.business.domain.Member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberDto {

    private final Long id;
    private final String name;
    private final Long groupId;

    public static MemberDto of(Member member) {
        return new MemberDto(
                member.getId(),
                member.getName(),
                getGroupIdOf(member)
        );
    }

    private static Long getGroupIdOf(Member member) {
        if (Objects.isNull(member.getGroup())) {
            return null;
        }
        return member.getGroup().getId();
    }

    @Override
    public String toString() {
        return "MemberDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", groupId=" + groupId +
                '}';
    }
}
