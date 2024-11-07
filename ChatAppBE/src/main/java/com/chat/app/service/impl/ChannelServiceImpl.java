package com.chat.app.service.impl;

import com.chat.app.exception.UserException;
import com.chat.app.model.Channel;
import com.chat.app.model.User;
import com.chat.app.repository.ChannelRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.service.IChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChannelServiceImpl implements IChannelService {

    private final ChannelRepository channelRepo;

    private final UserRepository userRepo;

    private final JwtServiceImpl jwtService;

    private String generateCustomId() {
        long count = channelRepo.count();
        return count < 10 ? "channel0" + (count + 1) : "channel" + (count + 1);
    }

    private String createNewChannel(User receiver, User sender) {
        if (!sender.getUserId().equals(receiver.getUserId())) {
            String customId = generateCustomId();
            Channel newChannel = new Channel();
            newChannel.setChannelId(customId);
            newChannel.setReceiver(receiver);
            newChannel.setSender(sender);
            newChannel.setListMessages(new ArrayList<>());
            channelRepo.save(newChannel);

            return newChannel.getChannelId();
        }
        return null;
    }

    @Override
    public String findChannelByUser(String token, Integer receiverId) throws UserException {
        try {
            String email = jwtService.extractUsername(token);
            User sender = userRepo.findByEmail(email).orElseThrow(() -> new UserException("Not found user " + email));
            User receiver = userRepo.findById(receiverId).orElseThrow(() -> new UserException("Not found receiver"));
            Optional<Channel> channel = channelRepo.findByReceiverAndSender(sender, receiver);

            String channelId;
            if (channel.isEmpty()) {
                channelId = createNewChannel(sender, receiver);
            } else {
                channelId = channel.get().getChannelId();
            }

            if (channelId == null) {
                throw new RuntimeException("Not found channel");
            }

            return channelId;
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
}
