package com.chat.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.chat.app.modal.Group;
import com.chat.app.modal.GroupMember;
import com.chat.app.modal.User;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, String> {

	Optional<GroupMember> findByUser(User user);
	
	Optional<GroupMember> findByUserAndGroup(User user, Group group);
	
	@Query("SELECT gm.group FROM GroupMember gm WHERE gm.user = :user")
	List<Group> findListGroupsByUser(User user);
}
