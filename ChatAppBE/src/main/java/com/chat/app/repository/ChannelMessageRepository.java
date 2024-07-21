package com.chat.app.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chat.app.modal.Channel;
import com.chat.app.modal.ChannelMessages;

@Repository
public interface ChannelMessageRepository extends JpaRepository<ChannelMessages, String> {

	List<ChannelMessages> findByChannel(Channel channel, Pageable pageable);
}