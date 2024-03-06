package com.snackgame.server.member.domain;

import javax.persistence.PostPersist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.snackgame.server.member.event.NewMemberEvent;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class MemberEntityListener {

    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public MemberEntityListener(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostPersist
    void postPersist(Member newMember) {
        if (newMember.getAccountType() != AccountType.GUEST) {
            eventPublisher.publishEvent(new NewMemberEvent(newMember.getNameAsString()));
        }
    }
}
