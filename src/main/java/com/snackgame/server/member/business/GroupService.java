package com.snackgame.server.member.business;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.member.business.domain.Group;
import com.snackgame.server.member.dao.GroupDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupDao groupDao;

    @Transactional
    public Group createIfNotExists(String name) {
        return groupDao.selectBy(name)
                .orElseGet(() -> groupDao.insert(new Group(name)));
    }

    @Transactional(readOnly = true)
    public Group findBy(Long id) {
        return groupDao.selectBy(id)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<String> findNamesStartWith(String prefix) {
        return groupDao.selectByNameLike(prefix).stream()
                .map(Group::getName)
                .collect(Collectors.toList());
    }
}
