package com.chat.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chat.app.modal.GroupMessages;

@Repository
public interface GroupMessageRepository extends JpaRepository<GroupMessages, String> {

}
