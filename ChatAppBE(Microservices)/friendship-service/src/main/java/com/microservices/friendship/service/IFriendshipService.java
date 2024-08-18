package com.microservices.friendship.service;

import com.microservices.friendship.dto.response.FriendshipResponse;

import java.util.List;

public interface IFriendshipService {

    FriendshipResponse sendAddFriend(Long friendId);

    FriendshipResponse acceptAddFriend(Long friendId);

    FriendshipResponse cancelAddFriend(Long friendId);

    List<FriendshipResponse> findFriendInListFriends(String friendName);

    int countRequestsAddFriend();

    List<FriendshipResponse> listUsersWaitingAccept();

    List<FriendshipResponse> listFriendsByUser();
}
