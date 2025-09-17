package com.nageoffer.shortlink.admin.controller;

import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.common.convention.result.Results;
import com.nageoffer.shortlink.admin.dto.req.GroupInsertReqDTO;
import com.nageoffer.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    /**
     * 新增短链组
     *
     * @param requestParam 新增组的名称
     */
    @PostMapping("/api/shortlink/v1/group/insert")
    public Result<Void> insertGroup(@RequestBody GroupInsertReqDTO requestParam){
        groupService.insetGroup(requestParam.getName());
        return Results.success();
    }
}
