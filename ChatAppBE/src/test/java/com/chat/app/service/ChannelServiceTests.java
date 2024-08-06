package com.chat.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.chat.app.exception.UserException;
import com.chat.app.model.Channel;
import com.chat.app.model.User;
import com.chat.app.repository.ChannelRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.service.impl.ChannelServiceImpl;
import com.chat.app.service.impl.JwtServiceImpl;

@SpringBootTest
public class ChannelServiceTests {

	@Autowired
	private ChannelServiceImpl channelService;
	
	@MockBean
	private ChannelRepository channelRepo;
	
	@MockBean
	private UserRepository userRepo;
	
	@MockBean
	private JwtServiceImpl jwtService;
	
	private final String token = "validToken";
	private final String senderEmail = "sender@gmail.com";
	private final String receiverEmail = "receiver@gmail.com";
	
	private User sender;
	private User receiver;
	private Channel channel;
	
	@BeforeEach
	void init() {
		sender = User.builder()
				.userId(1).email(senderEmail).userName("sender").build();
		receiver = User.builder()
				.userId(2).email(receiverEmail).userName("receiver").build();
		channel = Channel.builder()
				.channelId("channel01")
				.receiver(receiver)
				.sender(sender).build();
	}
	
	@Test
	void testFindChannelByUser_channelIsEmpty_success() throws UserException {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepo.findById(anyInt())).thenReturn(Optional.of(receiver));
		when(channelRepo.findByReceiverAndSender(sender, receiver)).thenReturn(Optional.empty());
		
		var res = channelService.findChannelByUser(token, 2);
		assertThat(res).isEqualTo("channel01");
	}
	
	@Test
	void testFindChannelByUser_channelIsExisted_success() throws UserException {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepo.findById(anyInt())).thenReturn(Optional.of(receiver));
		when(channelRepo.findByReceiverAndSender(sender, receiver)).thenReturn(Optional.of(channel));
		
		var res = channelService.findChannelByUser(token, 2);
		assertThat(res).isEqualTo("channel01");
	}
	
	@Test
	void testFindChannelByUser_notFoundSender_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> channelService.findChannelByUser(token, 2))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user " + senderEmail);
	}
	
	@Test
	void testFindChannelByUser_notFoundReceiver_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepo.findById(anyInt())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> channelService.findChannelByUser(token, 2))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found receiver");
	}
}
