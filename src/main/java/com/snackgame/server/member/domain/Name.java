package com.snackgame.server.member.domain;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.snackgame.server.member.exception.EmptyNameException;
import com.snackgame.server.member.exception.NameLengthException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Name {

    private static final Pattern NUMBERED_PATTERN = Pattern.compile(".+_\\d+");
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
        if (string.length() < 2 || string.length() > 16) {
            throw new NameLengthException();
        }
    }

    public Name nextAvailable() {
        if (NUMBERED_PATTERN.matcher(string).matches()) {
            int underscoreIndex = string.lastIndexOf('_');
            long currentNumber = Long.parseLong(string.substring(underscoreIndex + 1));
            return with(currentNumber + 1);
        }
        return with(2);
    }

    private Name with(long suffix) {
        int underscoreIndex = string.lastIndexOf('_');
        if (underscoreIndex > 0) {
            return new Name(string.substring(0, underscoreIndex) + '_' + suffix);
        }
        return new Name(string + '_' + suffix);
    }

    public String getString() {
        return string;
    }
}
