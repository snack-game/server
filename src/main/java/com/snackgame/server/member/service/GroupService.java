package com.snackgame.server.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.member.domain.Group;
import com.snackgame.server.member.domain.GroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groups;

    @Transactional
    public Group createIfNotExists(String name) {
        return groups.findByName(name)
                .orElseGet(() -> groups.save(new Group(name)));
    }

    public Group findBy(Long id) {
        return groups.findById(id)
                .orElse(null);
    }

    public List<String> findNamesStartWith(String prefix) {
        return groups.findByNameStartingWith(prefix).stream()
                .map(Group::getName)
                .collect(Collectors.toList());
    }
}
