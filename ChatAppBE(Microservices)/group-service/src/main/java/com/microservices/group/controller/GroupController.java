package com.microservices.group.controller;

import com.microservices.group.dto.request.GroupCreationRequest;
import com.microservices.group.dto.response.FriendshipResponse;
import com.microservices.group.dto.response.GroupResponse;
import com.microservices.group.dto.response.ProfileResponse;
import com.microservices.group.dto.response.ResponseData;
import com.microservices.group.service.impl.GroupServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
@Slf4j
public class GroupController {

    private final GroupServiceImpl groupService;

    @PostMapping("/create")
    public ResponseData<String> createNewGroup(@RequestBody GroupCreationRequest request) {
        try {
            return new ResponseData<>(HttpStatus.CREATED.value(), "Create group", groupService.createGroupChat(request));
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/{groupId}")
    public ResponseData<GroupResponse> findOneGroupById(@PathVariable("groupId") Long groupId) {
        try {
            log.info("Find group id {}", groupId);
            return new ResponseData<>(HttpStatus.OK.value(), "Find group",
                    groupService.findGroupById(groupId));
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @GetMapping("/members/{groupId}")
    public ResponseData<List<FriendshipResponse>> getMembersFromGroup(@PathVariable("groupId") Long groupId) {
        try {
            log.info("Members from group.");
            return new ResponseData<>(HttpStatus.OK.value(), "Members from group",
                    groupService.getListMembersForGroup(groupId));
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/lists-from-user")
    public ResponseData<List<GroupResponse>> getGroupsFromUser() {
        try {
            log.info("List groups from user.");
            return new ResponseData<>(HttpStatus.OK.value(), "List groups",
                    groupService.getListGroupsFromUser());
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/list-friends-invite/{group}")
    public ResponseData<List<ProfileResponse>> getListFriendsInvite(@PathVariable("group") Long groupId) {
        try {
            log.info("List users to invite.");
            return new ResponseData<>(HttpStatus.OK.value(), "List users to invite",
                    groupService.getListUsersToAddGroup(groupId));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("/add-user-to-group/{group}")
    public ResponseData<String> addUserToGroup(@PathVariable("group") Long groupId,
                                               @RequestParam("users") Set<Long> userIds) {
        try {
            log.info("Add user to group {}", groupId);
            return new ResponseData<>(HttpStatus.OK.value(), "Add user to group",
                    groupService.addFriendsToGroup(userIds, groupId));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("/remove-member-from-group/{group}/{user}")
    public ResponseData<String> removeMemberFromGroup(
            @PathVariable("group") Long groupId,
            @PathVariable("user") Long userId) {
        try {
            log.info("Remove member from group {}", groupId);
            return new ResponseData<>(HttpStatus.OK.value(), "Remove member from group",
                    groupService.removeMemberFromGroup(groupId, userId));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("/quit-group/{group}")
    public ResponseData<String> quitGroup(@PathVariable("group") Long groupId) {
        try {
            log.info("Quit group {}", groupId);
            return new ResponseData<>(HttpStatus.OK.value(), "Quit group",
                    groupService.quitGroup(groupId));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
