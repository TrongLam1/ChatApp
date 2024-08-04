package com.chat.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chat.app.model.Friendship;
import com.chat.app.model.User;
import com.chat.app.model.enums.StatusFriend;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, String> {

	@Query(value = "SELECT f.status FROM Friendship f WHERE f.user = :user AND f.friend = :friend")
	StatusFriend checkStatusFriend(@Param("user") User user,@Param("friend") User friend);
	
	Optional<Friendship> findByUserAndFriend(User user, User friend);
	
	@Query(value = "SELECT f FROM Friendship f WHERE f.user = :user")
	Set<Friendship> getRelationshipFromUser(@Param("user") User user);
	
	@Query(value = "SELECT f.friend FROM Friendship f WHERE f.user = :user AND f.status = :status")
	List<User> getListFriendsByUserAndStatus(@Param("user") User user, @Param("status") StatusFriend status);
	
	@Query(value = "SELECT f.user FROM Friendship f WHERE f.friend = :user AND f.status = :status")
	List<User> getListUsersByFriendAndStatus(@Param("user") User user, @Param("status") StatusFriend status);
	
	@Query(value = "SELECT COUNT(f) FROM Friendship f WHERE f.friend = :user AND f.status = :status")
	int countByUserAndStatus(@Param("user") User user, @Param("status") StatusFriend status);
}
