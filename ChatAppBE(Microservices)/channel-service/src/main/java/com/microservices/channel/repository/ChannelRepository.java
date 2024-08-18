package com.microservices.channel.repository;

import com.microservices.channel.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Optional<Channel> findByUserIdAndFriendId(Long userId, Long friendId);
}
