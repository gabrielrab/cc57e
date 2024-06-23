package br.edu.utfpr.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.utfpr.exception.ChatException;
import br.edu.utfpr.exception.MessageException;
import br.edu.utfpr.exception.UserException;
import br.edu.utfpr.modal.Chat;
import br.edu.utfpr.modal.Message;
import br.edu.utfpr.modal.User;
import br.edu.utfpr.repository.MessageRepository;
import br.edu.utfpr.request.SendMessageRequest;

@Service
public class MessageServiceImplement implements MessageService {
	@Autowired
	private MessageRepository messageRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private ChatService chatService;

	@Override
	public Message sendMessage(SendMessageRequest req) throws UserException, ChatException {
		User user = userService.findUserById(req.getUserId());
		Chat chat = chatService.findChatById(req.getChatId());

		Message message = new Message(req.getContent(), LocalDateTime.now(), false, user, chat);

		return messageRepo.save(message);
	}

	@Override
	public void deleteMessage(Integer messageId) throws MessageException {
		Message message = findMessageById(messageId);

		messageRepo.deleteById(message.getId());
	}

	@Override
	public List<Message> getChatsMessages(Integer chatId) throws ChatException {
		List<Message> messages = messageRepo.findMessageByChatId(chatId);

		return messages;
	}

	@Override
	public Message findMessageById(Integer messageId) throws MessageException {
		Optional<Message> message = messageRepo.findById(messageId);

		if (message.isPresent()) {
			return message.get();
		}
		throw new MessageException("a mensagem com o id " + messageId + " não existe");
	}

}
