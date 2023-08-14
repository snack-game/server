package com.snackgame.server.member.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.member.business.GroupService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "모든 그룹 이름 검색", description = "인자로 시작하는 모든 그룹 이름을 가져온다")
    @GetMapping("/groups/names")
    public List<String> showNamesStartWith(@RequestParam("startWith") String prefix) {
        return groupService.findNamesStartWith(prefix);
    }
}
