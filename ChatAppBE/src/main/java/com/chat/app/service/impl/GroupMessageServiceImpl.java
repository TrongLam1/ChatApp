package com.chat.app.service.impl;

import com.chat.app.dto.MessageDTO;
import com.chat.app.exception.GroupException;
import com.chat.app.exception.UserException;
import com.chat.app.model.Group;
import com.chat.app.model.GroupMember;
import com.chat.app.model.GroupMessages;
import com.chat.app.model.User;
import com.chat.app.repository.GroupMemberRepository;
import com.chat.app.repository.GroupMessageRepository;
import com.chat.app.repository.GroupRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.request.MessageRequest;
import com.chat.app.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupMessageServiceImpl implements IMessageService {

    private final GroupMessageRepository groupMessageRepository;

    private final GroupRepository groupRepo;

    private final GroupMemberRepository groupMemberRepository;

    private final JwtServiceImpl jwtService;

    private final UserRepository userRepo;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final CloudinaryServiceImpl cloudinaryService;

    private String generateCustomId() {
        long count = groupMessageRepository.count();
        return count < 10 ? "message0" + count : "message" + count;
    }

    @Override
    public void sendMessage(String token, MessageRequest request) {
        try {
            String emailSender = jwtService.extractUsername(token);
            User sender = userRepo.findByEmail(emailSender)
                    .orElseThrow(() -> new UserException("Not found user " + emailSender));

            Group group = groupRepo.findById(request.getSubscribe())
                    .orElseThrow(() -> new GroupException("Not found group " + request.getSubscribe()));

            LocalDateTime localDateTime = LocalDateTime.now();

            GroupMessages message = new GroupMessages();
            message.setContent(request.getContent());
            message.setGroup(group);
            message.setGroupMessageId(generateCustomId());
            message.setSender(sender);
            message.setImage_id(request.getImage_id());
            message.setImage_url(request.getImage_url());

            message = groupMessageRepository.save(message);

            group.setLastMessage(message);
            groupRepo.save(group);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
            String formattedDate = localDateTime.format(formatter);

            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setCreateAt(formattedDate);
            messageDTO.setContent(message.getContent());
            messageDTO.setSender(group.getGroupName());
            messageDTO.setImage_url(message.getImage_url());

            simpMessagingTemplate.convertAndSend("/channel/private/" + group.getGroupId(), messageDTO);
            for (GroupMember member : group.getListMembers()) {
                if (!member.getUser().getUserId().equals(sender.getUserId())) {
                    simpMessagingTemplate.convertAndSend("/channel/notify/" + member.getUser().getUserId(), messageDTO);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public List<MessageDTO> getListMessagesFromSubscribe(String token, String subscribelId) {
        try {
            String email = jwtService.extractUsername(token);
            User sender = userRepo.findByEmail(email)
                    .orElseThrow(() -> new UserException("Not found user " + email));
            Group group = groupRepo.findById(subscribelId)
                    .orElseThrow(() -> new GroupException("Not found group " + subscribelId));

            groupMemberRepository.findByUserAndGroup(sender, group)
                    .orElseThrow(() -> new UserException("Not found user in group"));

            List<GroupMessages> listMessages = group.getListMessages();

            return listMessages.stream()
                    .sorted(Comparator.comparing(GroupMessages::getCreateAt))
                    .map(item -> {
                        LocalDateTime localDateTime = item.getCreateAt();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
                        String formattedDate = localDateTime.format(formatter);

                        MessageDTO messageDTO = new MessageDTO();
                        messageDTO.setCreateAt(formattedDate);
                        messageDTO.setContent(item.getContent());
                        messageDTO.setSender(item.getSender().getUserName());
                        messageDTO.setImage_url(item.getImage_url());

                        return messageDTO;
                    }).toList();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public String sendImage(String token, MessageRequest message, MultipartFile file) {
        try {
            Map result = cloudinaryService.upload(file);
            message.setImage_url((String) result.get("url"));
            message.setImage_id((String) result.get("public_id"));
            sendMessage(token, message);
            return "Send img success.";
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
}
