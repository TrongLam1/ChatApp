package com.chat.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chat.app.model.Channel;
import com.chat.app.model.User;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, String> {

	@Query("SELECT c FROM Channel c WHERE (c.sender = :sender AND c.receiver = :receiver) OR "
			+ "(c.sender = :receiver AND c.receiver = :sender)")
	Optional<Channel> findByReceiverAndSender(@Param("receiver") User receiver, @Param("sender") User sender);

	Optional<Channel> findByChannelId(String id);
}
