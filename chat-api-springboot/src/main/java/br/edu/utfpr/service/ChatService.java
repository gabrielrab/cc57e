package br.edu.utfpr.service;

import java.util.List;

import br.edu.utfpr.exception.ChatException;
import br.edu.utfpr.exception.UserException;
import br.edu.utfpr.modal.Chat;
import br.edu.utfpr.request.GroupChatRequest;

public interface ChatService {

	Chat createChat(Integer reqUserId, Integer userId2, boolean isGroup) throws UserException;

	Chat findChatById(Integer chatId) throws ChatException;

	List<Chat> findAllChatByUserId(Integer userId) throws UserException;

	Chat createGroup(GroupChatRequest req, Integer reqUerId) throws UserException;

	Chat addUserToGroup(Integer userId, Integer chatId) throws UserException, ChatException;

	Chat renameGroup(Integer chatId, String groupName, Integer reqUserId) throws ChatException, UserException;

	Chat removeFromGroup(Integer chatId, Integer userId, Integer reqUser) throws UserException, ChatException;

	Chat deleteChat(Integer chatId, Integer userId) throws ChatException, UserException;

}
