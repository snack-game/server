package com.snackgame.server.member.business.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.snackgame.server.member.business.exception.EmptyNameException;
import com.snackgame.server.member.business.exception.NameLengthException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Name {

    @Column(name = "name")
    private String string;

    public Name(String string) {
        validateNotNull(string);
        validateLengthOf(string);
        this.string = string;
    }

    private void validateNotNull(String string) {
        if (Objects.isNull(string)) {
            throw new EmptyNameException();
        }
    }

    private void validateLengthOf(String string) {
        if (string.length() < 2) {
            throw new NameLengthException();
        }
    }

    public String getString() {
        return string;
    }
}
