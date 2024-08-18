package com.microservices.friendship.repository;

import com.microservices.friendship.entity.Friendship;
import com.microservices.friendship.entity.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<Friendship> findByUserIdAndUserFriendId(Long userId, Long userFriendId);

    @Query(value = "SELECT f FROM Friendship f WHERE f.userId = :userId AND f.status = :status")
    List<Friendship> findAllByUserIdAndStatus(@PathVariable Long userId, @PathVariable FriendshipStatus status);

    Integer countByUserIdAndStatus(Long userId, FriendshipStatus status);
}
