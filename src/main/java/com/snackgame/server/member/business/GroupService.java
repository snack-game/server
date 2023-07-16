package com.snackgame.server.member.business;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.member.business.domain.Group;
import com.snackgame.server.member.business.domain.GroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groups;

    @Transactional
    public Group createIfNotExists(String name) {
        return groups.findByName(name)
                .orElseGet(() -> groups.save(new Group(name)));
    }

    @Transactional(readOnly = true)
    public Group findBy(Long id) {
        return groups.findById(id)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<String> findNamesStartWith(String prefix) {
        return groups.findByNameStartingWith(prefix).stream()
                .map(Group::getName)
                .collect(Collectors.toList());
    }
}
