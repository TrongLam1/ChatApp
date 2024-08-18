package com.microservices.channel.repository;

import com.microservices.channel.entity.Channel;
import com.microservices.channel.entity.ChannelMessage;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelMessageRepository extends JpaRepository<ChannelMessage, Long> {

    @Query(value = "SELECT c FROM ChannelMessage c WHERE c.channel = :channel")
    List<ChannelMessage> getAllMessages(@Param Channel channel);
}
