package com.microservices.group.repository;

import com.microservices.group.entity.Group;
import com.microservices.group.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    Optional<GroupMember> findByUserIdAndGroup(Long userId, Group group);

    List<Group> findByUserId(Long userId);
}
