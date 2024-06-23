package br.edu.utfpr.controller;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.exception.ChatException;
import br.edu.utfpr.exception.UserException;
import br.edu.utfpr.modal.Chat;
import br.edu.utfpr.modal.Message;
import br.edu.utfpr.modal.User;
import br.edu.utfpr.request.SendMessageRequest;
import br.edu.utfpr.service.MessageService;
import br.edu.utfpr.service.UserService;

@RestController
public class RealTimeChat {
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@MessageMapping("/message")
	@SendTo("/group/public")
	public Message receiveMessage(@Payload Message message) {
		simpMessagingTemplate.convertAndSend("/group/" + message.getChat().getId().toString(), message);

		return message;
	}

	@MessageMapping("/chat/{groupId}")
	public Message sendToUser(@Payload SendMessageRequest req, @Header("Authorization") String jwt,
			@DestinationVariable String groupId) throws UserException, ChatException {
		User user = userService.findUserProfile(jwt);
		req.setUserId(user.getId());

		Message createdMessage = messageService.sendMessage(req);

		simpMessagingTemplate.convertAndSendToUser(groupId, "/private", createdMessage);

		return createdMessage;
	}

	public User reciver(Chat chat, User reqUser) {
		Iterator<User> iterator = chat.getUsers().iterator();

		User user1 = iterator.next();
		User user2 = iterator.next();

		if (user1.getId().equals(reqUser.getId())) {
			return user2;
		}
		return user1;
	}
}
