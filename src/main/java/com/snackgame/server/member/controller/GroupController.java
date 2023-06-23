package com.snackgame.server.member.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.member.business.GroupService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/groups/names")
    public List<String> showNamesStartWith(@RequestParam("startWith") String prefix) {
        return groupService.findNamesStartWith(prefix);
    }
}
