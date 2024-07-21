package com.chat.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chat.app.modal.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);
	
	List<User> findByUserNameContaining(String username);
}
