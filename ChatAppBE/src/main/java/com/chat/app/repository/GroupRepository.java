package com.chat.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chat.app.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {

	Optional<Group> findByGroupId(String id);
}
