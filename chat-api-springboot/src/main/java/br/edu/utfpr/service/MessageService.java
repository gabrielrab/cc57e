package br.edu.utfpr.service;

import java.util.List;

import br.edu.utfpr.exception.ChatException;
import br.edu.utfpr.exception.MessageException;
import br.edu.utfpr.exception.UserException;
import br.edu.utfpr.modal.Message;
import br.edu.utfpr.request.SendMessageRequest;

public interface MessageService {
	Message sendMessage(SendMessageRequest req) throws UserException, ChatException;

	List<Message> getChatsMessages(Integer chatId) throws ChatException;

	Message findMessageById(Integer messageId) throws MessageException;

	void deleteMessage(Integer messageId) throws MessageException;
}
