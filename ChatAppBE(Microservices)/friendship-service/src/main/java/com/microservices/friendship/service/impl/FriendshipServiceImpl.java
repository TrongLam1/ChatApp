package com.microservices.friendship.service.impl;

import com.microservices.friendship.dto.response.FriendshipResponse;
import com.microservices.friendship.dto.response.ProfileResponse;
import com.microservices.friendship.entity.Friendship;
import com.microservices.friendship.entity.enums.FriendshipStatus;
import com.microservices.friendship.exception.FriendshipException;
import com.microservices.friendship.repository.FriendshipRepository;
import com.microservices.friendship.repository.httpClient.ProfileClient;
import com.microservices.friendship.service.IFriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements IFriendshipService {

    private final FriendshipRepository friendshipRepository;

    private final ProfileClient profileClient;

    @Override
    public FriendshipResponse sendAddFriend(Long friendId) {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            ProfileResponse myProfile = profileClient.getProfileById(userId).getData();
            ProfileResponse friendProfile = profileClient.getProfileById(friendId).getData();

            Friendship myFriendship = Friendship.builder()
                    .userId(myProfile.getUserId())
                    .userFriendId(friendProfile.getUserId())
                    .createdDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .status(FriendshipStatus.WAITING)
                    .build();
            myFriendship = friendshipRepository.save(myFriendship);

            Friendship friendFriendship = Friendship.builder()
                    .userId(friendProfile.getUserId())
                    .userFriendId(myProfile.getUserId())
                    .createdDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .status(FriendshipStatus.PENDING)
                    .build();
            friendshipRepository.save(friendFriendship);

            return FriendshipResponse.builder()
                    .userId(friendProfile.getUserId())
                    .userName(friendProfile.getUsername())
                    .avatar(null)
                    .status(myFriendship.getStatus().toString())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public FriendshipResponse acceptAddFriend(Long friendId) {
        try {
            Long myId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            Friendship myFriendship = findFriendshipByUserId(myId, friendId);

            if (!myFriendship.getStatus().equals(FriendshipStatus.PENDING))
                throw new FriendshipException("Error");

            Friendship friendFriendship = findFriendshipByUserId(friendId, myId);

            myFriendship.setModifiedDate(LocalDateTime.now());
            myFriendship.setStatus(FriendshipStatus.FRIEND);
            friendshipRepository.save(myFriendship);

            friendFriendship.setModifiedDate(LocalDateTime.now());
            friendFriendship.setStatus(FriendshipStatus.FRIEND);
            friendshipRepository.save(friendFriendship);

            ProfileResponse friendProfile = profileClient.getProfileById(friendId).getData();

            return FriendshipResponse.builder()
                    .userId(friendProfile.getUserId())
                    .userName(friendProfile.getUsername())
                    .avatar(null)
                    .status(myFriendship.getStatus().toString())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public FriendshipResponse cancelAddFriend(Long friendId) {
        try {
            Long myId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            Friendship myFriendship = findFriendshipByUserId(friendId, myId);
            Friendship friendFriendship = findFriendshipByUserId(myId, friendId);

            friendshipRepository.delete(myFriendship);
            friendshipRepository.delete(friendFriendship);

            ProfileResponse friendProfile = profileClient.getProfileById(friendId).getData();

            return FriendshipResponse.builder()
                    .userId(friendProfile.getUserId())
                    .userName(friendProfile.getUsername())
                    .avatar(null)
                    .status(null)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public List<FriendshipResponse> findFriendInListFriends(String friendName) {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            List<ProfileResponse> listProfiles = profileClient.getProfilesByUsername(friendName).getData();
            List<Friendship> listFriends = friendshipRepository.findAllByUserIdAndStatus(userId, FriendshipStatus.FRIEND);

            List<FriendshipResponse> responseList = new ArrayList<>();

            for (ProfileResponse profile : listProfiles) {
                for (Friendship friend : listFriends) {
                    if (friend.getUserFriendId().equals(profile.getUserId())) {
                        FriendshipResponse friendshipResponse = FriendshipResponse.builder()
                                .userId(profile.getUserId())
                                .avatar(null)
                                .status(friend.getStatus().toString())
                                .userName(profile.getUsername())
                                .build();
                        responseList.add(friendshipResponse);
                    }
                }
            }

            return responseList;
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public int countRequestsAddFriend() {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            return friendshipRepository.countByUserIdAndStatus(userId, FriendshipStatus.PENDING);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public List<FriendshipResponse> listUsersWaitingAccept() {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            return friendshipRepository.findAllByUserIdAndStatus(userId, FriendshipStatus.PENDING)
                    .stream().map(friend -> {
                        ProfileResponse friendProfile = profileClient.getProfileById(friend.getUserId()).getData();
                        return FriendshipResponse.builder()
                                .userId(friendProfile.getUserId())
                                .avatar(null)
                                .status(friend.getStatus().toString())
                                .userName(friendProfile.getUsername())
                                .build();
                    }).toList();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public List<FriendshipResponse> listFriendsByUser() {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            return friendshipRepository.findAllByUserIdAndStatus(userId, FriendshipStatus.FRIEND)
                    .stream().map(friend -> {
                        ProfileResponse friendProfile = profileClient.getProfileById(friend.getUserId()).getData();
                        return FriendshipResponse.builder()
                                .userId(friendProfile.getUserId())
                                .avatar(null)
                                .status(friend.getStatus().toString())
                                .userName(friendProfile.getUsername())
                                .build();
                    }).toList();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public boolean checkedStatusFriend(Long friendId) {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            Friendship friendship = findFriendshipByUserId(userId, friendId);
            return friendship.getStatus().equals(FriendshipStatus.FRIEND);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    private Friendship findFriendshipByUserId(Long userId, Long userFriendId) throws FriendshipException {
        return friendshipRepository.findByUserIdAndUserFriendId(userId, userFriendId)
                .orElseThrow(() -> new FriendshipException("Not found friendship"));
    }
}
