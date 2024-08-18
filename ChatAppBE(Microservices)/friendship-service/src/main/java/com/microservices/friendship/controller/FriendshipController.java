package com.microservices.friendship.controller;

import com.microservices.friendship.dto.response.ResponseData;
import com.microservices.friendship.dto.response.ResponseError;
import com.microservices.friendship.service.impl.FriendshipServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friendship")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipServiceImpl friendshipService;

    @PostMapping("/add-friend/{friendId}")
    public ResponseData<?> requestFriend(@PathVariable Long friendId) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Request friend", friendshipService.sendAddFriend(friendId));
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/accept-friend/{friendId}")
    public ResponseData<?> acceptFriend(@PathVariable Long friendId) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Accept friend", friendshipService.acceptAddFriend(friendId));
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("/cancel-friend/{friendId}")
    public ResponseData<?> cancelFriend(@PathVariable Long friendId) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Cancel friend", friendshipService.cancelAddFriend(friendId));
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/list-friends")
    public ResponseData<?> getListFriendsFromUser() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "List friends", friendshipService.listFriendsByUser());
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/list-friends-waiting-accept")
    public ResponseData<?> getListFriendsWaitingAcceptFromUser() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "List waiting accept friends",
                    friendshipService.listUsersWaitingAccept());
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/find-friends-by-username/{username}")
    public ResponseData<?> findFriendsByUsername(@PathVariable String username) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Friends by username",
                    friendshipService.findFriendInListFriends(username));
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
